package co.edu.unbosque.conducar.repository;

import co.edu.unbosque.conducar.model.consulta.ResultadoTabla;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class ConsultaRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public ConsultaRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    
    public ResultadoTabla ejecutar(String sql) {
        return jdbc.getJdbcTemplate().query(sql, rs -> {
            ResultSetMetaData meta = rs.getMetaData();
            List<String> columnas = new ArrayList<>();
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                columnas.add(meta.getColumnLabel(i));
            }
            List<List<Object>> filas = new ArrayList<>();
            while (rs.next()) {
                List<Object> fila = new ArrayList<>();
                for (int i = 1; i <= columnas.size(); i++) {
                    Object valor = rs.getObject(i);
                    boolean esFecha = valor instanceof java.util.Date || valor instanceof java.time.temporal.Temporal;
                    fila.add(esFecha ? valor.toString() : valor);
                }
                filas.add(fila);
            }
            return new ResultadoTabla(columnas, filas);
        });
    }
}
