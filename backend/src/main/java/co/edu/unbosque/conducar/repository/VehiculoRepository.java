package co.edu.unbosque.conducar.repository;

import co.edu.unbosque.conducar.model.vehiculo.VehiculoResumen;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/** Consultas de VEHICULO con el estado del SOAT y de la revisión técnico-mecánica. */
public class VehiculoRepository extends RepositorioBase {

    // Los cuatro ? son, en orden: hoy, hoy + 30 días (SOAT), hoy, hoy + 30 días (RTM).
    private static final String VEHICULO_BASE = """
            SELECT v.placa, v.codigo_categoria, v.tipo_vehiculo, v.marca, v.linea, v.modelo_anio, v.transmision, v.estado,
                   v.fecha_venc_soat,
                   CASE WHEN v.fecha_venc_soat < ? THEN 'VENCIDO'
                        WHEN v.fecha_venc_soat < ? THEN 'POR VENCER'
                        ELSE 'VIGENTE' END AS estado_soat,
                   v.fecha_venc_rtm,
                   CASE WHEN v.fecha_venc_rtm < ? THEN 'VENCIDO'
                        WHEN v.fecha_venc_rtm < ? THEN 'POR VENCER'
                        ELSE 'VIGENTE' END AS estado_rtm,
                   (SELECT COALESCE(SUM(cl.horas), 0) FROM clase cl
                     WHERE cl.placa = v.placa AND cl.estado = 'DICTADA') AS horas_uso,
                   (SELECT COUNT(*) FROM clase cl
                     WHERE cl.placa = v.placa AND cl.estado = 'PROGRAMADA') AS clases_programadas
            FROM vehiculo v
            """;

    public VehiculoRepository(ConexionBD conexionBD) {
        super(conexionBD);
    }

    private static VehiculoResumen mapear(ResultSet rs) throws SQLException {
        return new VehiculoResumen(
                rs.getString("placa"), rs.getString("codigo_categoria"), rs.getString("tipo_vehiculo"),
                rs.getString("marca"), rs.getString("linea"), entero(rs, "modelo_anio"), rs.getString("transmision"),
                rs.getString("estado"), fecha(rs, "fecha_venc_soat"), rs.getString("estado_soat"),
                fecha(rs, "fecha_venc_rtm"), rs.getString("estado_rtm"), decimal(rs, "horas_uso"),
                entero(rs, "clases_programadas"));
    }

    public List<VehiculoResumen> listar(LocalDate hoy) throws SQLException {
        String sql = VEHICULO_BASE + "ORDER BY v.codigo_categoria, v.placa";
        LocalDate en30 = hoy.plusDays(30);
        return consultar(sql, VehiculoRepository::mapear, hoy, en30, hoy, en30);
    }

    public Optional<VehiculoResumen> buscarPorPlaca(String placa, LocalDate hoy) throws SQLException {
        String sql = VEHICULO_BASE + "WHERE v.placa = ?";
        LocalDate en30 = hoy.plusDays(30);
        return consultarUno(sql, VehiculoRepository::mapear, hoy, en30, hoy, en30, placa);
    }

    public List<String> placas() throws SQLException {
        return consultar("SELECT placa FROM vehiculo ORDER BY placa", rs -> rs.getString("placa"));
    }
}
