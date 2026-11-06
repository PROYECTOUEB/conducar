package co.edu.unbosque.conducar.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * Lee la configuración del archivo config.properties (carpeta resources).
 * Si existe una variable de entorno con el mismo nombre, esa tiene prioridad.
 */
public class Configuracion {

    private final Properties propiedades = new Properties();

    public Configuracion() {
        try (InputStream in = Configuracion.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) {
                throw new IllegalStateException("No se encontró config.properties en src/main/resources");
            }
            propiedades.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo leer config.properties", e);
        }
    }

    public String valor(String clave) {
        String entorno = System.getenv(clave);
        if (entorno != null && !entorno.isBlank()) {
            return entorno.trim();
        }
        String valor = propiedades.getProperty(clave);
        if (valor == null) {
            throw new IllegalStateException("Falta el valor " + clave + " en config.properties");
        }
        return valor.trim();
    }

    public int entero(String clave) {
        return Integer.parseInt(valor(clave));
    }
}
