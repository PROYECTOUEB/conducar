package co.edu.unbosque.conducar.repository;

import co.edu.unbosque.conducar.model.matricula.MatriculaResumen;
import java.util.List;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class MatriculaRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public MatriculaRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    
    public List<MatriculaResumen> listar(String estado) {
        MapSqlParameterSource p = new MapSqlParameterSource();
        String where = "";
        if (estado != null) {
            where = "WHERE m.estado = :estado\n";
            p.addValue("estado", estado);
        }
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
        return jdbc.query(sql, p, new DataClassRowMapper<>(MatriculaResumen.class));
    }
}
