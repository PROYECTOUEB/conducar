package co.edu.unbosque.conducar.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tabla de rutas GET de la API. Relaciona cada ruta (por ejemplo /api/alumnos/{id})
 * con el método del controlador que la atiende.
 */
public class Router {

    private record Ruta(String[] partes, Accion accion) {
    }

    /** Resultado de buscar una ruta: la acción y los parámetros tomados de la URL. */
    public record Coincidencia(Accion accion, Map<String, String> parametros) {
    }

    private final List<Ruta> rutas = new ArrayList<>();

    public Router get(String patron, Accion accion) {
        rutas.add(new Ruta(dividir(patron), accion));
        return this;
    }

    public Coincidencia buscar(String camino) {
        String[] partes = dividir(camino);
        for (Ruta r : rutas) {
            if (r.partes().length != partes.length) continue;
            Map<String, String> parametros = new HashMap<>();
            boolean coincide = true;
            for (int i = 0; i < partes.length && coincide; i++) {
                String p = r.partes()[i];
                if (p.startsWith("{") && p.endsWith("}")) {
                    parametros.put(p.substring(1, p.length() - 1), partes[i]);
                } else {
                    coincide = p.equals(partes[i]);
                }
            }
            if (coincide) {
                return new Coincidencia(r.accion(), parametros);
            }
        }
        return null;
    }

    private static String[] dividir(String camino) {
        String limpio = camino.replaceAll("^/+|/+$", "");
        return limpio.isEmpty() ? new String[0] : limpio.split("/");
    }
}
