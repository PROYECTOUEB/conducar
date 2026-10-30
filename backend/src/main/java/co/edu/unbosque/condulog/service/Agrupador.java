package co.edu.unbosque.condulog.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Convierte filas (clave, valor) en un mapa clave -> "valor1, valor2, ...". */
final class Agrupador {

    private Agrupador() {
    }

    static Map<Integer, String> agrupar(List<Map<String, Object>> filas, String clave, String valor) {
        Map<Integer, String> resultado = new LinkedHashMap<>();
        for (Map<String, Object> fila : filas) {
            Integer k = ((Number) fila.get(clave)).intValue();
            String v = String.valueOf(fila.get(valor));
            resultado.merge(k, v, (anterior, nuevo) -> anterior + ", " + nuevo);
        }
        return resultado;
    }
}
