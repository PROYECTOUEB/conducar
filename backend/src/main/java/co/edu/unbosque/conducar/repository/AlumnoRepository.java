package co.edu.unbosque.conducar.repository;

import co.edu.unbosque.conducar.model.alumno.AlumnoDatos;
import co.edu.unbosque.conducar.model.alumno.AlumnoResumen;
import co.edu.unbosque.conducar.model.alumno.CertificadoCrc;
import co.edu.unbosque.conducar.model.alumno.ExamenAlumno;
import co.edu.unbosque.conducar.model.alumno.MatriculaAvance;
import co.edu.unbosque.conducar.model.alumno.PagoAlumno;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/** Acceso a datos de ALUMNO y sus certificados, matrículas, exámenes y pagos. */
@Repository
public class AlumnoRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public AlumnoRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /** Lista de alumnos; si se envía texto, filtra por nombre, apellido o documento. */
    public List<AlumnoResumen> buscar(String texto) {
        MapSqlParameterSource p = new MapSqlParameterSource();
        String filtro = "";
        if (texto != null) {
            filtro = """
                    WHERE CONCAT(a.nombres, ' ', a.apellidos) LIKE :patron
                       OR a.numero_documento LIKE :patron
                    """;
            p.addValue("patron", "%" + texto + "%");
        }
        String sql = """
                SELECT a.id_alumno, a.tipo_documento, a.numero_documento, a.nombres, a.apellidos, a.correo, a.telefono,
                       (SELECT MAX(m.codigo_categoria) FROM matricula m
                         WHERE m.id_alumno = a.id_alumno AND m.estado = 'ACTIVA') AS categoria_activa,
                       (SELECT COALESCE(SUM(cl.horas), 0) FROM clase cl JOIN matricula m ON m.id_matricula = cl.id_matricula
                         WHERE m.id_alumno = a.id_alumno AND cl.estado = 'DICTADA') AS horas_dictadas,
                       CASE WHEN a.id_alumno IN (SELECT id_alumno FROM certificado_crc WHERE resultado = 'APTO') THEN 'APTO'
                            WHEN a.id_alumno IN (SELECT id_alumno FROM certificado_crc) THEN 'NO APTO'
                            ELSE 'SIN REGISTRO' END AS estado_crc
                FROM alumno a
                """ + filtro + "ORDER BY a.apellidos, a.nombres";
        return jdbc.query(sql, p, new DataClassRowMapper<>(AlumnoResumen.class));
    }

    public Optional<AlumnoDatos> buscarPorId(int id) {
        String sql = """
                SELECT id_alumno, tipo_documento, numero_documento, nombres, apellidos, fecha_nacimiento,
                       telefono, correo, fecha_registro, NULL AS edad
                FROM alumno
                WHERE id_alumno = :id
                """;
        return jdbc.query(sql, new MapSqlParameterSource("id", id), new DataClassRowMapper<>(AlumnoDatos.class))
                .stream().findFirst();
    }

    public List<CertificadoCrc> certificados(int id) {
        String sql = """
                SELECT nombre_crc, fecha_examen, resultado
                FROM certificado_crc
                WHERE id_alumno = :id
                ORDER BY fecha_examen DESC
                """;
        return jdbc.query(sql, new MapSqlParameterSource("id", id), new DataClassRowMapper<>(CertificadoCrc.class));
    }

    /** Matrículas con lo pagado y las horas dictadas frente a las del plan, por tipo de clase. */
    public List<MatriculaAvance> matriculas(int id) {
        String sql = """
                SELECT m.id_matricula, m.codigo_categoria, c.descripcion, m.fecha_matricula, m.estado, m.valor_pactado,
                       (SELECT COALESCE(SUM(p.valor), 0) FROM pago p WHERE p.id_matricula = m.id_matricula) AS total_pagado,
                       (SELECT COALESCE(SUM(pc.horas), 0) FROM plan_curso pc JOIN modulo md ON md.id_modulo = pc.id_modulo
                         WHERE pc.codigo_categoria = m.codigo_categoria AND md.tipo = 'TEORIA') AS horas_teoria_plan,
                       (SELECT COALESCE(SUM(cl.horas), 0) FROM clase cl JOIN modulo md ON md.id_modulo = cl.id_modulo
                         WHERE cl.id_matricula = m.id_matricula AND cl.estado = 'DICTADA' AND md.tipo = 'TEORIA') AS horas_teoria_dictadas,
                       (SELECT COALESCE(SUM(pc.horas), 0) FROM plan_curso pc JOIN modulo md ON md.id_modulo = pc.id_modulo
                         WHERE pc.codigo_categoria = m.codigo_categoria AND md.tipo = 'TALLER') AS horas_taller_plan,
                       (SELECT COALESCE(SUM(cl.horas), 0) FROM clase cl JOIN modulo md ON md.id_modulo = cl.id_modulo
                         WHERE cl.id_matricula = m.id_matricula AND cl.estado = 'DICTADA' AND md.tipo = 'TALLER') AS horas_taller_dictadas,
                       (SELECT COALESCE(SUM(pc.horas), 0) FROM plan_curso pc JOIN modulo md ON md.id_modulo = pc.id_modulo
                         WHERE pc.codigo_categoria = m.codigo_categoria AND md.tipo = 'PRACTICA') AS horas_practica_plan,
                       (SELECT COALESCE(SUM(cl.horas), 0) FROM clase cl JOIN modulo md ON md.id_modulo = cl.id_modulo
                         WHERE cl.id_matricula = m.id_matricula AND cl.estado = 'DICTADA' AND md.tipo = 'PRACTICA') AS horas_practica_dictadas,
                       ca.numero_certificado
                FROM matricula m
                JOIN categoria_licencia c ON c.codigo_categoria = m.codigo_categoria
                LEFT JOIN certificado_aptitud ca ON ca.id_matricula = m.id_matricula
                WHERE m.id_alumno = :id
                ORDER BY m.fecha_matricula DESC
                """;
        return jdbc.query(sql, new MapSqlParameterSource("id", id), new DataClassRowMapper<>(MatriculaAvance.class));
    }

    public List<ExamenAlumno> examenes(int id) {
        String sql = """
                SELECT e.id_examen, e.id_matricula, m.codigo_categoria, e.tipo, e.fecha, e.puntaje,
                       CASE WHEN e.puntaje >= 80 THEN 'APROBADO' ELSE 'REPROBADO' END AS resultado,
                       CONCAT(i.nombres, ' ', i.apellidos) AS instructor
                FROM examen e
                JOIN matricula m ON m.id_matricula = e.id_matricula
                JOIN instructor i ON i.id_instructor = e.id_instructor
                WHERE m.id_alumno = :id
                ORDER BY e.fecha DESC
                """;
        return jdbc.query(sql, new MapSqlParameterSource("id", id), new DataClassRowMapper<>(ExamenAlumno.class));
    }

    public List<PagoAlumno> pagos(int id) {
        String sql = """
                SELECT p.id_pago, p.id_matricula, p.fecha_pago, p.valor, p.medio_pago
                FROM pago p
                JOIN matricula m ON m.id_matricula = p.id_matricula
                WHERE m.id_alumno = :id
                ORDER BY p.fecha_pago DESC
                """;
        return jdbc.query(sql, new MapSqlParameterSource("id", id), new DataClassRowMapper<>(PagoAlumno.class));
    }
}
