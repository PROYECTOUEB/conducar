package co.edu.unbosque.condulog.repository;

import co.edu.unbosque.condulog.model.instructor.InstructorOpcion;
import co.edu.unbosque.condulog.model.instructor.InstructorResumen;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/** Acceso a datos de INSTRUCTOR e INSTRUCTOR_CATEGORIA. */
@Repository
public class InstructorRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public InstructorRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<InstructorResumen> listar(LocalDate hoy, LocalDate en60) {
        String sql = """
                SELECT i.id_instructor, CONCAT(i.nombres, ' ', i.apellidos) AS nombre, i.correo, i.telefono,
                       i.numero_licencia_instructor AS numero_licencia, i.fecha_venc_licencia,
                       CASE WHEN i.fecha_venc_licencia < :hoy THEN 'VENCIDA'
                            WHEN i.fecha_venc_licencia < :en60 THEN 'POR VENCER'
                            ELSE 'VIGENTE' END AS estado_licencia,
                       i.estado,
                       (SELECT COALESCE(SUM(cl.horas), 0) FROM clase cl
                         WHERE cl.id_instructor = i.id_instructor AND cl.estado = 'DICTADA') AS horas_dictadas,
                       (SELECT COUNT(*) FROM clase cl
                         WHERE cl.id_instructor = i.id_instructor AND cl.estado = 'PROGRAMADA') AS clases_programadas,
                       NULL AS categorias
                FROM instructor i
                ORDER BY nombre
                """;
        MapSqlParameterSource p = new MapSqlParameterSource("hoy", hoy).addValue("en60", en60);
        return jdbc.query(sql, p, new DataClassRowMapper<>(InstructorResumen.class));
    }

    /** Categorías que enseña cada instructor: lista de pares (id_instructor, codigo_categoria). */
    public List<Map<String, Object>> categorias() {
        String sql = """
                SELECT id_instructor, codigo_categoria
                FROM instructor_categoria
                ORDER BY codigo_categoria
                """;
        return jdbc.queryForList(sql, new MapSqlParameterSource());
    }

    /** Instructores para los filtros de la interfaz. */
    public List<InstructorOpcion> opciones() {
        String sql = """
                SELECT id_instructor, CONCAT(nombres, ' ', apellidos) AS nombre
                FROM instructor
                ORDER BY nombre
                """;
        return jdbc.query(sql, new MapSqlParameterSource(), new DataClassRowMapper<>(InstructorOpcion.class));
    }
}
