package co.edu.unbosque.conducar.repository;

import co.edu.unbosque.conducar.model.instructor.InstructorOpcion;
import co.edu.unbosque.conducar.model.instructor.InstructorResumen;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/** Consultas de INSTRUCTOR y de sus habilitaciones (INSTRUCTOR_CATEGORIA). */
public class InstructorRepository extends RepositorioBase {

    public InstructorRepository(ConexionBD conexionBD) {
        super(conexionBD);
    }

    public List<InstructorResumen> listar(LocalDate hoy, LocalDate en60) throws SQLException {
        String sql = """
                SELECT i.id_instructor, i.numero_documento, i.nombres, i.apellidos,
                       CONCAT(i.nombres, ' ', i.apellidos) AS nombre, i.telefono, i.correo,
                       i.numero_licencia_instructor AS numero_licencia, i.fecha_venc_licencia,
                       CASE WHEN i.fecha_venc_licencia < ? THEN 'VENCIDA'
                            WHEN i.fecha_venc_licencia < ? THEN 'POR VENCER'
                            ELSE 'VIGENTE' END AS estado_licencia,
                       i.estado,
                       (SELECT COALESCE(SUM(cl.horas), 0) FROM clase cl
                         WHERE cl.id_instructor = i.id_instructor AND cl.estado = 'DICTADA') AS horas_dictadas,
                       (SELECT COUNT(*) FROM clase cl
                         WHERE cl.id_instructor = i.id_instructor AND cl.estado = 'PROGRAMADA') AS clases_programadas
                FROM instructor i
                ORDER BY i.id_instructor
                """;
        return consultar(sql, rs -> new InstructorResumen(
                entero(rs, "id_instructor"), rs.getString("numero_documento"), rs.getString("nombres"),
                rs.getString("apellidos"), rs.getString("nombre"), rs.getString("telefono"), rs.getString("correo"),
                rs.getString("numero_licencia"), fecha(rs, "fecha_venc_licencia"), rs.getString("estado_licencia"),
                rs.getString("estado"), decimal(rs, "horas_dictadas"), entero(rs, "clases_programadas"), null),
                hoy, en60);
    }

    /** Pares (id_instructor, codigo_categoria) de las habilitaciones. */
    public List<String[]> categorias() throws SQLException {
        String sql = """
                SELECT id_instructor, codigo_categoria
                FROM instructor_categoria
                ORDER BY codigo_categoria
                """;
        return consultar(sql, rs -> new String[] {rs.getString("id_instructor"), rs.getString("codigo_categoria")});
    }

    /** Lista para el filtro de instructores de la agenda. */
    public List<InstructorOpcion> opciones() throws SQLException {
        String sql = """
                SELECT id_instructor, CONCAT(nombres, ' ', apellidos) AS nombre
                FROM instructor
                ORDER BY nombre
                """;
        return consultar(sql, rs -> new InstructorOpcion(entero(rs, "id_instructor"), rs.getString("nombre")));
    }
}
