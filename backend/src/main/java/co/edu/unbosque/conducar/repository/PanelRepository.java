package co.edu.unbosque.conducar.repository;

import co.edu.unbosque.conducar.model.panel.Alerta;
import co.edu.unbosque.conducar.model.panel.ClasesPorMes;
import co.edu.unbosque.conducar.model.panel.HorasInstructor;
import co.edu.unbosque.conducar.model.panel.Indicadores;
import co.edu.unbosque.conducar.model.panel.MatriculasCategoria;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/** Consultas del panel principal: indicadores, alertas y estadísticas. */
public class PanelRepository extends RepositorioBase {

    public PanelRepository(ConexionBD conexionBD) {
        super(conexionBD);
    }

    public Indicadores indicadores() throws SQLException {
        String sql = """
                SELECT
                  (SELECT COUNT(DISTINCT id_alumno) FROM matricula WHERE estado = 'ACTIVA') AS alumnos_activos,
                  (SELECT COUNT(*) FROM matricula WHERE estado = 'ACTIVA') AS matriculas_activas,
                  (SELECT COUNT(*) FROM clase WHERE estado = 'PROGRAMADA') AS clases_programadas,
                  (SELECT COALESCE(SUM(horas), 0) FROM clase WHERE estado = 'DICTADA') AS horas_dictadas,
                  (SELECT COUNT(*) FROM vehiculo WHERE estado = 'DISPONIBLE') AS vehiculos_disponibles,
                  (SELECT COUNT(*) FROM vehiculo) AS vehiculos,
                  (SELECT COUNT(*) FROM instructor WHERE estado = 'ACTIVO') AS instructores_activos,
                  (SELECT COALESCE(SUM(m.valor_pactado), 0) FROM matricula m WHERE m.estado = 'ACTIVA')
                    - (SELECT COALESCE(SUM(p.valor), 0) FROM pago p JOIN matricula m ON m.id_matricula = p.id_matricula
                        WHERE m.estado = 'ACTIVA') AS saldo_pendiente,
                  (SELECT ROUND(100.0 * SUM(CASE WHEN puntaje >= 80 THEN 1 ELSE 0 END) / COUNT(*), 1) FROM examen) AS tasa_aprobacion
                """;
        return consultarUno(sql, rs -> new Indicadores(
                entero(rs, "alumnos_activos"), entero(rs, "matriculas_activas"), entero(rs, "clases_programadas"),
                decimal(rs, "horas_dictadas"), entero(rs, "vehiculos_disponibles"), entero(rs, "vehiculos"),
                entero(rs, "instructores_activos"), decimal(rs, "saldo_pendiente"), decimal(rs, "tasa_aprobacion")))
                .orElseThrow();
    }

    /**
     * Alertas de SOAT, revisión técnico-mecánica, vehículos fuera de servicio, licencias de instructor
     * y alumnos sin examen del CRC. Los ? se asignan en el orden en que aparecen en el SQL.
     */
    public List<Alerta> alertas(LocalDate hoy, LocalDate en30, LocalDate en60) throws SQLException {
        String sql = """
                SELECT tipo, sujeto, detalle, fecha, nivel, enlace FROM (
                  SELECT 'SOAT' AS tipo, CONCAT(v.placa, ' · ', v.marca, ' ', v.linea) AS sujeto,
                         CASE WHEN v.fecha_venc_soat < ? THEN 'Vencido' ELSE 'Vence en menos de 30 días' END AS detalle,
                         v.fecha_venc_soat AS fecha,
                         CASE WHEN v.fecha_venc_soat < ? THEN 'alta' ELSE 'media' END AS nivel,
                         CONCAT('/vehiculos/', v.placa) AS enlace
                  FROM vehiculo v
                  WHERE v.fecha_venc_soat < ?
                  UNION ALL
                  SELECT 'Revisión técnico-mecánica', CONCAT(v.placa, ' · ', v.marca, ' ', v.linea),
                         CASE WHEN v.fecha_venc_rtm < ? THEN 'Vencida' ELSE 'Vence en menos de 30 días' END,
                         v.fecha_venc_rtm, CASE WHEN v.fecha_venc_rtm < ? THEN 'alta' ELSE 'media' END,
                         CONCAT('/vehiculos/', v.placa)
                  FROM vehiculo v
                  WHERE v.fecha_venc_rtm < ?
                  UNION ALL
                  SELECT 'Vehículo fuera de servicio', CONCAT(v.placa, ' · ', v.marca, ' ', v.linea),
                         REPLACE(v.estado, '_', ' '), NULL, 'media', CONCAT('/vehiculos/', v.placa)
                  FROM vehiculo v
                  WHERE v.estado <> 'DISPONIBLE'
                  UNION ALL
                  SELECT 'Licencia de instructor', CONCAT(i.nombres, ' ', i.apellidos),
                         CASE WHEN i.fecha_venc_licencia < ? THEN 'Vencida' ELSE 'Vence en menos de 60 días' END,
                         i.fecha_venc_licencia, CASE WHEN i.fecha_venc_licencia < ? THEN 'alta' ELSE 'media' END,
                         '/instructores'
                  FROM instructor i
                  WHERE i.estado = 'ACTIVO' AND i.fecha_venc_licencia < ?
                  UNION ALL
                  SELECT 'Alumno sin examen del CRC', CONCAT(a.nombres, ' ', a.apellidos),
                         'No puede iniciar clases sin el certificado médico', a.fecha_registro, 'alta',
                         CONCAT('/alumnos/', a.id_alumno)
                  FROM alumno a
                  WHERE a.id_alumno IN (SELECT id_alumno FROM matricula WHERE estado = 'ACTIVA')
                    AND a.id_alumno NOT IN (SELECT id_alumno FROM certificado_crc WHERE resultado = 'APTO')
                ) alertas
                ORDER BY CASE nivel WHEN 'alta' THEN 1 WHEN 'media' THEN 2 ELSE 3 END, fecha
                """;
        return consultar(sql, rs -> new Alerta(
                rs.getString("tipo"), rs.getString("sujeto"), rs.getString("detalle"), fecha(rs, "fecha"),
                rs.getString("nivel"), rs.getString("enlace")),
                hoy, hoy, en30,      // SOAT
                hoy, hoy, en30,      // revisión técnico-mecánica
                hoy, hoy, en60);     // licencia del instructor
    }

    public List<HorasInstructor> horasPorInstructor() throws SQLException {
        String sql = """
                SELECT CONCAT(i.nombres, ' ', i.apellidos) AS instructor, COALESCE(SUM(cl.horas), 0) AS horas
                FROM instructor i
                LEFT JOIN clase cl ON cl.id_instructor = i.id_instructor AND cl.estado = 'DICTADA'
                GROUP BY i.id_instructor, i.nombres, i.apellidos
                ORDER BY horas DESC
                """;
        return consultar(sql, rs -> new HorasInstructor(rs.getString("instructor"), decimal(rs, "horas")));
    }

    public List<MatriculasCategoria> matriculasPorCategoria() throws SQLException {
        String sql = """
                SELECT codigo_categoria AS categoria, COUNT(*) AS matriculas
                FROM matricula
                GROUP BY codigo_categoria
                ORDER BY matriculas DESC
                """;
        return consultar(sql, rs -> new MatriculasCategoria(rs.getString("categoria"), entero(rs, "matriculas")));
    }

    public List<ClasesPorMes> clasesPorMes() throws SQLException {
        String sql = """
                SELECT EXTRACT(YEAR FROM fecha) AS anio, EXTRACT(MONTH FROM fecha) AS mes,
                       SUM(CASE WHEN estado = 'DICTADA' THEN 1 ELSE 0 END) AS dictadas,
                       SUM(CASE WHEN estado = 'CANCELADA' THEN 1 ELSE 0 END) AS canceladas
                FROM clase
                WHERE estado <> 'PROGRAMADA'
                GROUP BY EXTRACT(YEAR FROM fecha), EXTRACT(MONTH FROM fecha)
                ORDER BY anio, mes
                """;
        return consultar(sql, rs -> new ClasesPorMes(
                String.format("%04d-%02d", rs.getInt("anio"), rs.getInt("mes")),
                rs.getInt("dictadas"), rs.getInt("canceladas")));
    }
}
