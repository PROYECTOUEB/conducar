package co.edu.unbosque.conducar.repository;

import co.edu.unbosque.conducar.model.estado.ServidorInfo;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/** Datos del servidor MySQL y conteo de registros (para comprobar la conexión). */
public class EstadoRepository extends RepositorioBase {

    private static final String[] TABLAS = {"alumno", "instructor", "vehiculo", "matricula", "clase", "examen"};

    public EstadoRepository(ConexionBD conexionBD) {
        super(conexionBD);
    }

    public ServidorInfo servidor() throws SQLException {
        String sql = """
                SELECT VERSION() AS version, DATABASE() AS base_datos, CURRENT_USER() AS usuario,
                       @@hostname AS servidor, @@port AS puerto, CAST(NOW() AS CHAR) AS fecha_servidor,
                       @@transaction_read_only AS solo_lectura
                """;
        return consultarUno(sql, rs -> new ServidorInfo(
                "MySQL " + rs.getString("version"), rs.getString("base_datos"), rs.getString("usuario"),
                rs.getString("servidor"), entero(rs, "puerto"), rs.getString("fecha_servidor"),
                rs.getInt("solo_lectura") == 1)).orElseThrow();
    }

    public int numeroTablas() throws SQLException {
        String sql = "SELECT COUNT(*) AS n FROM information_schema.tables WHERE table_schema = DATABASE()";
        return consultarUno(sql, rs -> rs.getInt("n")).orElse(0);
    }

    public Map<String, Integer> conteos() throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT ");
        for (int i = 0; i < TABLAS.length; i++) {
            sql.append(i == 0 ? "" : ", ").append("(SELECT COUNT(*) FROM ").append(TABLAS[i]).append(") AS ").append(TABLAS[i]);
        }
        return consultarUno(sql.toString(), rs -> {
            Map<String, Integer> conteos = new LinkedHashMap<>();
            for (String tabla : TABLAS) {
                conteos.put(tabla, rs.getInt(tabla));
            }
            return conteos;
        }).orElseThrow();
    }
}
