package co.edu.unbosque.condulog.repository;

import co.edu.unbosque.condulog.model.categoria.Categoria;
import co.edu.unbosque.condulog.model.categoria.ModuloPlan;
import java.util.List;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/** Acceso a datos de CATEGORIA_LICENCIA, PLAN_CURSO y MODULO. */
@Repository
public class CategoriaRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public CategoriaRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Categoria> categorias() {
        String sql = """
                SELECT c.codigo_categoria, c.descripcion, c.tipo_servicio, c.edad_minima, c.valor_curso,
                       (SELECT COALESCE(SUM(p.horas), 0) FROM plan_curso p
                         WHERE p.codigo_categoria = c.codigo_categoria) AS horas_totales,
                       (SELECT COUNT(*) FROM matricula m
                         WHERE m.codigo_categoria = c.codigo_categoria AND m.estado = 'ACTIVA') AS matriculas_activas,
                       (SELECT COUNT(*) FROM matricula m WHERE m.codigo_categoria = c.codigo_categoria) AS matriculas
                FROM categoria_licencia c
                ORDER BY c.codigo_categoria
                """;
        // Los módulos del plan se agregan después en el servicio
        return jdbc.query(sql, new MapSqlParameterSource(), (rs, fila) -> new Categoria(
                rs.getString("codigo_categoria"), rs.getString("descripcion"), rs.getString("tipo_servicio"),
                rs.getInt("edad_minima"), valorODefecto(rs.getBigDecimal("valor_curso")), rs.getInt("horas_totales"),
                rs.getInt("matriculas_activas"), rs.getInt("matriculas"), List.of()));
    }

    /** El valor del curso es nulo cuando la escuela no ofrece esa categoría. */
    private static Double valorODefecto(java.math.BigDecimal valor) {
        return valor == null ? null : valor.doubleValue();
    }

    public List<ModuloPlan> plan() {
        String sql = """
                SELECT p.codigo_categoria, md.id_modulo, md.nombre, md.tipo, md.descripcion, p.horas
                FROM plan_curso p
                JOIN modulo md ON md.id_modulo = p.id_modulo
                ORDER BY p.codigo_categoria, md.id_modulo
                """;
        return jdbc.query(sql, new MapSqlParameterSource(), new DataClassRowMapper<>(ModuloPlan.class));
    }
}
