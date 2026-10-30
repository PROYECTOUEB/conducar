package co.edu.unbosque.conducar.model.consulta;

import java.util.List;


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
