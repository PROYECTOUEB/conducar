package co.edu.unbosque.conducar.repository;

import co.edu.unbosque.conducar.model.estado.ServidorInfo;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/** Consultas sobre el servidor MySQL y el tamaño de la base de datos. */
@Repository
public class EstadoRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public EstadoRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public ServidorInfo servidor() {
        String sql = """
                SELECT VERSION() AS version, DATABASE() AS base_datos, CURRENT_USER() AS usuario,
                       @@hostname AS servidor, @@port AS puerto, CAST(NOW() AS CHAR) AS fecha_servidor,
                       @@transaction_read_only AS solo_lectura
                """;
        return jdbc.queryForObject(sql, new MapSqlParameterSource(), (rs, fila) -> new ServidorInfo(
                "MySQL " + rs.getString("version"), rs.getString("base_datos"), rs.getString("usuario"),
                rs.getString("servidor"), rs.getInt("puerto"), rs.getString("fecha_servidor"),
                rs.getInt("solo_lectura") == 1));
    }

    public int numeroTablas() {
        String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE()";
        Integer n = jdbc.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
        return n == null ? 0 : n;
    }

    /** Número de registros de las tablas principales. */
    public Map<String, Integer> conteos() {
        String sql = """
                SELECT (SELECT COUNT(*) FROM alumno) AS alumno,
                       (SELECT COUNT(*) FROM instructor) AS instructor,
                       (SELECT COUNT(*) FROM vehiculo) AS vehiculo,
                       (SELECT COUNT(*) FROM matricula) AS matricula,
                       (SELECT COUNT(*) FROM clase) AS clase,
                       (SELECT COUNT(*) FROM examen) AS examen
                """;
        return jdbc.queryForObject(sql, new MapSqlParameterSource(), (rs, fila) -> {
            Map<String, Integer> conteos = new LinkedHashMap<>();
            for (String tabla : new String[] {"alumno", "instructor", "vehiculo", "matricula", "clase", "examen"}) {
                conteos.put(tabla, rs.getInt(tabla));
            }
            return conteos;
        });
    }
}
