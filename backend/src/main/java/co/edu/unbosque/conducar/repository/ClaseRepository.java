package co.edu.unbosque.conducar.repository;

import co.edu.unbosque.conducar.model.clase.ClaseResumen;
import co.edu.unbosque.conducar.model.clase.FiltroClases;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/** Acceso a datos de la tabla CLASE y sus tablas relacionadas. */
@Repository
public class ClaseRepository {

    /** Consulta base: clase + módulo + matrícula + alumno + instructor. */
    private static final String CLASE_BASE = """
            SELECT cl.id_clase, cl.fecha, cl.hora_inicio, cl.horas, cl.estado, md.tipo, md.nombre AS modulo,
                   m.codigo_categoria, cl.id_matricula, a.id_alumno, CONCAT(a.nombres, ' ', a.apellidos) AS alumno,
                   cl.id_instructor, CONCAT(i.nombres, ' ', i.apellidos) AS instructor, cl.placa
            FROM clase cl
            JOIN modulo md ON md.id_modulo = cl.id_modulo
            JOIN matricula m ON m.id_matricula = cl.id_matricula
            JOIN alumno a ON a.id_alumno = m.id_alumno
            JOIN instructor i ON i.id_instructor = cl.id_instructor
            """;

    private final NamedParameterJdbcTemplate jdbc;

    public ClaseRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /** Agenda de clases con filtros opcionales (máximo 500, de la más reciente a la más antigua). */
    public List<ClaseResumen> buscar(FiltroClases f) {
        List<String> condiciones = new ArrayList<>();
        MapSqlParameterSource p = new MapSqlParameterSource();
        if (f.estado() != null) { condiciones.add("cl.estado = :estado"); p.addValue("estado", f.estado()); }
        if (f.tipo() != null) { condiciones.add("md.tipo = :tipo"); p.addValue("tipo", f.tipo()); }
        if (f.idInstructor() != null) { condiciones.add("cl.id_instructor = :instructor"); p.addValue("instructor", f.idInstructor()); }
        if (f.placa() != null) { condiciones.add("cl.placa = :placa"); p.addValue("placa", f.placa()); }
        if (f.desde() != null) { condiciones.add("cl.fecha >= :desde"); p.addValue("desde", f.desde()); }
        if (f.hasta() != null) { condiciones.add("cl.fecha <= :hasta"); p.addValue("hasta", f.hasta()); }
        String where = condiciones.isEmpty() ? "" : "WHERE " + String.join(" AND ", condiciones) + "\n";
        String sql = CLASE_BASE + where + "ORDER BY cl.fecha DESC, cl.hora_inicio DESC LIMIT 500";
        return jdbc.query(sql, p, new DataClassRowMapper<>(ClaseResumen.class));
    }

    /** Próximas clases programadas. */
    public List<ClaseResumen> proximas(int cantidad) {
        String sql = CLASE_BASE + "WHERE cl.estado = 'PROGRAMADA' ORDER BY cl.fecha, cl.hora_inicio LIMIT :cantidad";
        return jdbc.query(sql, new MapSqlParameterSource("cantidad", cantidad), new DataClassRowMapper<>(ClaseResumen.class));
    }

    /** Clases de un alumno (todas sus matrículas). */
    public List<ClaseResumen> porAlumno(int idAlumno) {
        String sql = CLASE_BASE + "WHERE a.id_alumno = :id ORDER BY cl.fecha DESC, cl.hora_inicio DESC";
        return jdbc.query(sql, new MapSqlParameterSource("id", idAlumno), new DataClassRowMapper<>(ClaseResumen.class));
    }

    /** Últimas clases en las que se usó un vehículo. */
    public List<ClaseResumen> porVehiculo(String placa, int cantidad) {
        String sql = CLASE_BASE + "WHERE cl.placa = :placa ORDER BY cl.fecha DESC, cl.hora_inicio DESC LIMIT :cantidad";
        MapSqlParameterSource p = new MapSqlParameterSource("placa", placa).addValue("cantidad", cantidad);
        return jdbc.query(sql, p, new DataClassRowMapper<>(ClaseResumen.class));
    }
}
