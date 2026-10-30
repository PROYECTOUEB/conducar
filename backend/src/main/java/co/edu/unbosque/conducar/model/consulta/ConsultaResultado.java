package co.edu.unbosque.conducar.model.consulta;

import java.util.List;

/** Consulta con el resultado de ejecutar su SQL. */
public record ConsultaResultado(
        Integer n,
        String categoria,
        String pregunta,
        String explicacion,
        String sql,
        List<String> columnas,
        List<List<Object>> filas,
        Integer total) {
}
