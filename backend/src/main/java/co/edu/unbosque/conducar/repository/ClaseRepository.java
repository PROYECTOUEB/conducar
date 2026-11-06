package co.edu.unbosque.conducar.repository;

import co.edu.unbosque.conducar.model.clase.ClaseResumen;
import co.edu.unbosque.conducar.model.clase.FiltroClases;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Consultas de la tabla CLASE con su módulo, matrícula, alumno e instructor. */
public class ClaseRepository extends RepositorioBase {

    private static final String CLASE_BASE = """
            SELECT cl.id_clase, cl.fecha, cl.hora_inicio, cl.horas, cl.estado, cl.id_modulo, md.tipo, md.nombre AS modulo,
                   m.codigo_categoria, cl.id_matricula, a.id_alumno, CONCAT(a.nombres, ' ', a.apellidos) AS alumno,
                   cl.id_instructor, CONCAT(i.nombres, ' ', i.apellidos) AS instructor, cl.placa
            FROM clase cl
            JOIN modulo md ON md.id_modulo = cl.id_modulo
            JOIN matricula m ON m.id_matricula = cl.id_matricula
            JOIN alumno a ON a.id_alumno = m.id_alumno
            JOIN instructor i ON i.id_instructor = cl.id_instructor
            """;

    public ClaseRepository(ConexionBD conexionBD) {
        super(conexionBD);
    }

    /** Convierte una fila en ClaseResumen. */
    static ClaseResumen mapear(ResultSet rs) throws SQLException {
        return new ClaseResumen(
                entero(rs, "id_clase"), fecha(rs, "fecha"), hora(rs, "hora_inicio"), entero(rs, "horas"),
                rs.getString("estado"), entero(rs, "id_modulo"), rs.getString("tipo"), rs.getString("modulo"),
                rs.getString("codigo_categoria"), entero(rs, "id_matricula"), entero(rs, "id_alumno"),
                rs.getString("alumno"), entero(rs, "id_instructor"), rs.getString("instructor"), rs.getString("placa"));
    }

    /** Agenda con filtros opcionales: solo se agregan al WHERE los filtros que llegan. */
    public List<ClaseResumen> buscar(FiltroClases f) throws SQLException {
        List<String> condiciones = new ArrayList<>();
        List<Object> parametros = new ArrayList<>();
        if (f.estado() != null) { condiciones.add("cl.estado = ?"); parametros.add(f.estado()); }
        if (f.tipo() != null) { condiciones.add("md.tipo = ?"); parametros.add(f.tipo()); }
        if (f.idInstructor() != null) { condiciones.add("cl.id_instructor = ?"); parametros.add(f.idInstructor()); }
        if (f.placa() != null) { condiciones.add("cl.placa = ?"); parametros.add(f.placa()); }
        if (f.desde() != null) { condiciones.add("cl.fecha >= ?"); parametros.add(f.desde()); }
        if (f.hasta() != null) { condiciones.add("cl.fecha <= ?"); parametros.add(f.hasta()); }
        String where = condiciones.isEmpty() ? "" : "WHERE " + String.join(" AND ", condiciones) + "\n";
        String sql = CLASE_BASE + where + "ORDER BY cl.fecha DESC, cl.hora_inicio DESC LIMIT 500";
        return consultar(sql, ClaseRepository::mapear, parametros.toArray());
    }

    /** Próximas clases programadas para el panel. */
    public List<ClaseResumen> proximas(int cantidad) throws SQLException {
        String sql = CLASE_BASE + "WHERE cl.estado = 'PROGRAMADA' ORDER BY cl.fecha, cl.hora_inicio LIMIT ?";
        return consultar(sql, ClaseRepository::mapear, cantidad);
    }

    public List<ClaseResumen> porAlumno(int idAlumno) throws SQLException {
        String sql = CLASE_BASE + "WHERE a.id_alumno = ? ORDER BY cl.fecha DESC, cl.hora_inicio DESC";
        return consultar(sql, ClaseRepository::mapear, idAlumno);
    }

    /** Últimas clases en las que se usó un vehículo. */
    public List<ClaseResumen> porVehiculo(String placa, int cantidad) throws SQLException {
        String sql = CLASE_BASE + "WHERE cl.placa = ? ORDER BY cl.fecha DESC, cl.hora_inicio DESC LIMIT ?";
        return consultar(sql, ClaseRepository::mapear, placa, cantidad);
    }
}
