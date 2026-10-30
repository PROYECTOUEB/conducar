package co.edu.unbosque.conducar.repository;

import co.edu.unbosque.conducar.model.vehiculo.VehiculoResumen;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/** Acceso a datos de VEHICULO. */
@Repository
public class VehiculoRepository {

    /** Vehículo con el estado de sus documentos y su uso en clases. */
    private static final String VEHICULO_BASE = """
            SELECT v.placa, v.codigo_categoria, v.tipo_vehiculo, v.marca, v.linea, v.modelo_anio, v.transmision, v.estado,
                   v.fecha_venc_soat,
                   CASE WHEN v.fecha_venc_soat < :hoy THEN 'VENCIDO'
                        WHEN v.fecha_venc_soat < :en30 THEN 'POR VENCER'
                        ELSE 'VIGENTE' END AS estado_soat,
                   v.fecha_venc_rtm,
                   CASE WHEN v.fecha_venc_rtm < :hoy THEN 'VENCIDO'
                        WHEN v.fecha_venc_rtm < :en30 THEN 'POR VENCER'
                        ELSE 'VIGENTE' END AS estado_rtm,
                   (SELECT COALESCE(SUM(cl.horas), 0) FROM clase cl
                     WHERE cl.placa = v.placa AND cl.estado = 'DICTADA') AS horas_uso,
                   (SELECT COUNT(*) FROM clase cl
                     WHERE cl.placa = v.placa AND cl.estado = 'PROGRAMADA') AS clases_programadas
            FROM vehiculo v
            """;

    private final NamedParameterJdbcTemplate jdbc;

    public VehiculoRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<VehiculoResumen> listar(LocalDate hoy) {
        String sql = VEHICULO_BASE + "ORDER BY v.codigo_categoria, v.placa";
        return jdbc.query(sql, fechas(hoy), new DataClassRowMapper<>(VehiculoResumen.class));
    }

    public Optional<VehiculoResumen> buscarPorPlaca(String placa, LocalDate hoy) {
        String sql = VEHICULO_BASE + "WHERE v.placa = :placa";
        return jdbc.query(sql, fechas(hoy).addValue("placa", placa), new DataClassRowMapper<>(VehiculoResumen.class))
                .stream().findFirst();
    }

    /** Placas de todos los vehículos (para los filtros). */
    public List<String> placas() {
        return jdbc.queryForList("SELECT placa FROM vehiculo ORDER BY placa", new MapSqlParameterSource(), String.class);
    }

    private MapSqlParameterSource fechas(LocalDate hoy) {
        return new MapSqlParameterSource("hoy", hoy).addValue("en30", hoy.plusDays(30));
    }
}
