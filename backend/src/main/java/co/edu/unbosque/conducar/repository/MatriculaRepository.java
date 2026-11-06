package co.edu.unbosque.conducar.repository;

import co.edu.unbosque.conducar.model.matricula.MatriculaResumen;

import java.sql.SQLException;
import java.util.List;

/** Consultas de MATRICULA con lo pagado, el saldo y las horas. */
public class MatriculaRepository extends RepositorioBase {

    public MatriculaRepository(ConexionBD conexionBD) {
        super(conexionBD);
    }

    /** Matrículas; si llega un estado, filtra por él. El saldo se calcula, no se guarda. */
    public List<MatriculaResumen> listar(String estado) throws SQLException {
        String where = estado == null ? "" : "WHERE m.estado = ?\n";
        Object[] parametros = estado == null ? new Object[] {} : new Object[] {estado};
        String sql = """
                SELECT m.id_matricula, a.id_alumno, CONCAT(a.nombres, ' ', a.apellidos) AS alumno, m.codigo_categoria,
                       m.fecha_matricula, m.estado, m.valor_pactado,
                       (SELECT COALESCE(SUM(pg.valor), 0) FROM pago pg WHERE pg.id_matricula = m.id_matricula) AS total_pagado,
                       m.valor_pactado - (SELECT COALESCE(SUM(pg.valor), 0) FROM pago pg
                                           WHERE pg.id_matricula = m.id_matricula) AS saldo,
                       (SELECT COALESCE(SUM(cl.horas), 0) FROM clase cl
                         WHERE cl.id_matricula = m.id_matricula AND cl.estado = 'DICTADA') AS horas_dictadas,
                       (SELECT SUM(pc.horas) FROM plan_curso pc WHERE pc.codigo_categoria = m.codigo_categoria) AS horas_plan
                FROM matricula m
                JOIN alumno a ON a.id_alumno = m.id_alumno
                """ + where + "ORDER BY m.id_matricula";
        return consultar(sql, rs -> new MatriculaResumen(
                entero(rs, "id_matricula"), entero(rs, "id_alumno"), rs.getString("alumno"),
                rs.getString("codigo_categoria"), fecha(rs, "fecha_matricula"), rs.getString("estado"),
                decimal(rs, "valor_pactado"), decimal(rs, "total_pagado"), decimal(rs, "saldo"),
                entero(rs, "horas_dictadas"), entero(rs, "horas_plan")), parametros);
    }
}
