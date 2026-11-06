package co.edu.unbosque.conducar.repository;

import co.edu.unbosque.conducar.model.categoria.Categoria;
import co.edu.unbosque.conducar.model.categoria.ModuloPlan;

import java.sql.SQLException;
import java.util.List;

/** Consultas de CATEGORIA_LICENCIA y de su plan de curso (PLAN_CURSO + MODULO). */
public class CategoriaRepository extends RepositorioBase {

    public CategoriaRepository(ConexionBD conexionBD) {
        super(conexionBD);
    }

    public List<Categoria> categorias() throws SQLException {
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
        return consultar(sql, rs -> new Categoria(
                rs.getString("codigo_categoria"), rs.getString("descripcion"), rs.getString("tipo_servicio"),
                entero(rs, "edad_minima"), decimal(rs, "valor_curso"), entero(rs, "horas_totales"),
                entero(rs, "matriculas_activas"), entero(rs, "matriculas"), List.of()));
    }

    public List<ModuloPlan> plan() throws SQLException {
        String sql = """
                SELECT p.codigo_categoria, md.id_modulo, md.nombre, md.tipo, md.descripcion, p.horas
                FROM plan_curso p
                JOIN modulo md ON md.id_modulo = p.id_modulo
                ORDER BY p.codigo_categoria, md.id_modulo
                """;
        return consultar(sql, rs -> new ModuloPlan(
                rs.getString("codigo_categoria"), entero(rs, "id_modulo"), rs.getString("nombre"),
                rs.getString("tipo"), rs.getString("descripcion"), entero(rs, "horas")));
    }
}
