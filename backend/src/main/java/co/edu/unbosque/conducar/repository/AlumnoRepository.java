package co.edu.unbosque.conducar.repository;

import co.edu.unbosque.conducar.model.alumno.AlumnoDatos;
import co.edu.unbosque.conducar.model.alumno.AlumnoResumen;
import co.edu.unbosque.conducar.model.alumno.CertificadoCrc;
import co.edu.unbosque.conducar.model.alumno.ExamenAlumno;
import co.edu.unbosque.conducar.model.alumno.MatriculaAvance;
import co.edu.unbosque.conducar.model.alumno.PagoAlumno;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/** Consultas de la tabla ALUMNO y de lo relacionado con cada alumno. */
public class AlumnoRepository extends RepositorioBase {

    public AlumnoRepository(ConexionBD conexionBD) {
        super(conexionBD);
    }

    /** Lista de alumnos; si llega texto, filtra por nombre o documento. */
    public List<AlumnoResumen> buscar(String texto) throws SQLException {
        String filtro = "";
        Object[] parametros = {};
        if (texto != null) {
            filtro = """
                    WHERE CONCAT(a.nombres, ' ', a.apellidos) LIKE ?
                       OR a.numero_documento LIKE ?
                    """;
            String patron = "%" + texto + "%";
            parametros = new Object[] {patron, patron};
        }
        String sql = """
                SELECT a.id_alumno, a.tipo_documento, a.numero_documento, a.nombres, a.apellidos, a.fecha_nacimiento,
                       a.telefono, a.correo, a.fecha_registro,
                       (SELECT MAX(m.codigo_categoria) FROM matricula m
                         WHERE m.id_alumno = a.id_alumno AND m.estado = 'ACTIVA') AS categoria_activa,
                       (SELECT COALESCE(SUM(cl.horas), 0) FROM clase cl JOIN matricula m ON m.id_matricula = cl.id_matricula
                         WHERE m.id_alumno = a.id_alumno AND cl.estado = 'DICTADA') AS horas_dictadas,
                       CASE WHEN a.id_alumno IN (SELECT id_alumno FROM certificado_crc WHERE resultado = 'APTO') THEN 'APTO'
                            WHEN a.id_alumno IN (SELECT id_alumno FROM certificado_crc) THEN 'NO APTO'
                            ELSE 'SIN REGISTRO' END AS estado_crc
                FROM alumno a
                """ + filtro + "ORDER BY a.id_alumno";
        return consultar(sql, rs -> new AlumnoResumen(
                entero(rs, "id_alumno"), rs.getString("tipo_documento"), rs.getString("numero_documento"),
                rs.getString("nombres"), rs.getString("apellidos"), fecha(rs, "fecha_nacimiento"),
                rs.getString("telefono"), rs.getString("correo"), fecha(rs, "fecha_registro"),
                rs.getString("categoria_activa"), decimal(rs, "horas_dictadas"), rs.getString("estado_crc")), parametros);
    }

    public Optional<AlumnoDatos> buscarPorId(int id) throws SQLException {
        String sql = """
                SELECT id_alumno, tipo_documento, numero_documento, nombres, apellidos, fecha_nacimiento,
                       telefono, correo, fecha_registro
                FROM alumno
                WHERE id_alumno = ?
                """;
        return consultarUno(sql, rs -> new AlumnoDatos(
                entero(rs, "id_alumno"), rs.getString("tipo_documento"), rs.getString("numero_documento"),
                rs.getString("nombres"), rs.getString("apellidos"), fecha(rs, "fecha_nacimiento"),
                rs.getString("telefono"), rs.getString("correo"), fecha(rs, "fecha_registro"), null), id);
    }

    public List<CertificadoCrc> certificados(int id) throws SQLException {
        String sql = """
                SELECT nombre_crc, fecha_examen, resultado
                FROM certificado_crc
                WHERE id_alumno = ?
                ORDER BY fecha_examen DESC
                """;
        return consultar(sql, rs -> new CertificadoCrc(
                rs.getString("nombre_crc"), fecha(rs, "fecha_examen"), rs.getString("resultado")), id);
    }

    /** Matrículas del alumno con lo pagado y el avance de horas por tipo de módulo. */
    public List<MatriculaAvance> matriculas(int id) throws SQLException {
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
                WHERE m.id_alumno = ?
                ORDER BY m.fecha_matricula DESC
                """;
        return consultar(sql, rs -> new MatriculaAvance(
                entero(rs, "id_matricula"), rs.getString("codigo_categoria"), rs.getString("descripcion"),
                fecha(rs, "fecha_matricula"), rs.getString("estado"), decimal(rs, "valor_pactado"),
                decimal(rs, "total_pagado"), entero(rs, "horas_teoria_plan"), entero(rs, "horas_teoria_dictadas"),
                entero(rs, "horas_taller_plan"), entero(rs, "horas_taller_dictadas"),
                entero(rs, "horas_practica_plan"), entero(rs, "horas_practica_dictadas"),
                rs.getString("numero_certificado")), id);
    }

    public List<ExamenAlumno> examenes(int id) throws SQLException {
        String sql = """
                SELECT e.id_examen, e.id_matricula, m.codigo_categoria, e.tipo, e.fecha, e.puntaje,
                       CASE WHEN e.puntaje >= 80 THEN 'APROBADO' ELSE 'REPROBADO' END AS resultado,
                       CONCAT(i.nombres, ' ', i.apellidos) AS instructor
                FROM examen e
                JOIN matricula m ON m.id_matricula = e.id_matricula
                JOIN instructor i ON i.id_instructor = e.id_instructor
                WHERE m.id_alumno = ?
                ORDER BY e.fecha DESC
                """;
        return consultar(sql, rs -> new ExamenAlumno(
                entero(rs, "id_examen"), entero(rs, "id_matricula"), rs.getString("codigo_categoria"),
                rs.getString("tipo"), fecha(rs, "fecha"), decimal(rs, "puntaje"), rs.getString("resultado"),
                rs.getString("instructor")), id);
    }

    public List<PagoAlumno> pagos(int id) throws SQLException {
        String sql = """
                SELECT p.id_pago, p.id_matricula, p.fecha_pago, p.valor, p.medio_pago
                FROM pago p
                JOIN matricula m ON m.id_matricula = p.id_matricula
                WHERE m.id_alumno = ?
                ORDER BY p.fecha_pago DESC
                """;
        return consultar(sql, rs -> new PagoAlumno(
                entero(rs, "id_pago"), entero(rs, "id_matricula"), fecha(rs, "fecha_pago"),
                decimal(rs, "valor"), rs.getString("medio_pago")), id);
    }
}
