package co.edu.unbosque.conducar.repository;

import co.edu.unbosque.conducar.model.panel.Alerta;
import co.edu.unbosque.conducar.model.panel.ClasesPorMes;
import co.edu.unbosque.conducar.model.panel.HorasInstructor;
import co.edu.unbosque.conducar.model.panel.Indicadores;
import co.edu.unbosque.conducar.model.panel.MatriculasCategoria;
import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class PanelRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public PanelRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Indicadores indicadores() {
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
        return jdbc.queryForObject(sql, new MapSqlParameterSource(), new DataClassRowMapper<>(Indicadores.class));
    }

    
    public List<Alerta> alertas(LocalDate hoy, LocalDate en30, LocalDate en60) {
        String sql = """
                SELECT tipo, sujeto, detalle, fecha, nivel, enlace FROM (
                  SELECT 'SOAT' AS tipo, CONCAT(v.placa, ' · ', v.marca, ' ', v.linea) AS sujeto,
                         CASE WHEN v.fecha_venc_soat < :hoy THEN 'Vencido' ELSE 'Vence en menos de 30 días' END AS detalle,
                         v.fecha_venc_soat AS fecha,
                         CASE WHEN v.fecha_venc_soat < :hoy THEN 'alta' ELSE 'media' END AS nivel,
                         CONCAT('/vehiculos/', v.placa) AS enlace
                  FROM vehiculo v
                  WHERE v.fecha_venc_soat < :en30
                  UNION ALL
                  SELECT 'Revisión técnico-mecánica', CONCAT(v.placa, ' · ', v.marca, ' ', v.linea),
                         CASE WHEN v.fecha_venc_rtm < :hoy THEN 'Vencida' ELSE 'Vence en menos de 30 días' END,
                         v.fecha_venc_rtm, CASE WHEN v.fecha_venc_rtm < :hoy THEN 'alta' ELSE 'media' END,
                         CONCAT('/vehiculos/', v.placa)
                  FROM vehiculo v
                  WHERE v.fecha_venc_rtm < :en30
                  UNION ALL
                  SELECT 'Vehículo fuera de servicio', CONCAT(v.placa, ' · ', v.marca, ' ', v.linea),
                         REPLACE(v.estado, '_', ' '), NULL, 'media', CONCAT('/vehiculos/', v.placa)
                  FROM vehiculo v
                  WHERE v.estado <> 'DISPONIBLE'
                  UNION ALL
                  SELECT 'Licencia de instructor', CONCAT(i.nombres, ' ', i.apellidos),
                         CASE WHEN i.fecha_venc_licencia < :hoy THEN 'Vencida' ELSE 'Vence en menos de 60 días' END,
                         i.fecha_venc_licencia, CASE WHEN i.fecha_venc_licencia < :hoy THEN 'alta' ELSE 'media' END,
                         '/instructores'
                  FROM instructor i
                  WHERE i.estado = 'ACTIVO' AND i.fecha_venc_licencia < :en60
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
        MapSqlParameterSource p = new MapSqlParameterSource("hoy", hoy).addValue("en30", en30).addValue("en60", en60);
        return jdbc.query(sql, p, new DataClassRowMapper<>(Alerta.class));
    }

    public List<HorasInstructor> horasPorInstructor() {
        String sql = """
                SELECT CONCAT(i.nombres, ' ', i.apellidos) AS instructor, COALESCE(SUM(cl.horas), 0) AS horas
                FROM instructor i
                LEFT JOIN clase cl ON cl.id_instructor = i.id_instructor AND cl.estado = 'DICTADA'
                GROUP BY i.id_instructor, i.nombres, i.apellidos
                ORDER BY horas DESC
                """;
        return jdbc.query(sql, new MapSqlParameterSource(), new DataClassRowMapper<>(HorasInstructor.class));
    }

    public List<MatriculasCategoria> matriculasPorCategoria() {
        String sql = """
                SELECT codigo_categoria AS categoria, COUNT(*) AS matriculas
                FROM matricula
                GROUP BY codigo_categoria
                ORDER BY matriculas DESC
                """;
        return jdbc.query(sql, new MapSqlParameterSource(), new DataClassRowMapper<>(MatriculasCategoria.class));
    }

    public List<ClasesPorMes> clasesPorMes() {
        String sql = """
                SELECT EXTRACT(YEAR FROM fecha) AS anio, EXTRACT(MONTH FROM fecha) AS mes,
                       SUM(CASE WHEN estado = 'DICTADA' THEN 1 ELSE 0 END) AS dictadas,
                       SUM(CASE WHEN estado = 'CANCELADA' THEN 1 ELSE 0 END) AS canceladas
                FROM clase
                WHERE estado <> 'PROGRAMADA'
                GROUP BY EXTRACT(YEAR FROM fecha), EXTRACT(MONTH FROM fecha)
                ORDER BY anio, mes
                """;
        return jdbc.query(sql, new MapSqlParameterSource(), (rs, fila) -> new ClasesPorMes(
                String.format("%04d-%02d", rs.getInt("anio"), rs.getInt("mes")),
                rs.getInt("dictadas"), rs.getInt("canceladas")));
    }
}
