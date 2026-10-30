package co.edu.unbosque.conducar.model.consulta;

import java.util.List;

/** Resultado genérico de una consulta: nombres de columnas y filas. */
public record ResultadoTabla(
        List<String> columnas,
        List<List<Object>> filas) {
}
