package co.edu.unbosque.conducar.model.consulta;

import java.util.List;


public record ResultadoTabla(
        List<String> columnas,
        List<List<Object>> filas) {
}
