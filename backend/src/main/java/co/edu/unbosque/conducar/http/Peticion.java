package co.edu.unbosque.conducar.http;

import co.edu.unbosque.conducar.exception.SolicitudInvalidaException;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

/** Datos de una petición HTTP: parámetros de la ruta (/api/alumnos/{id}) y de la consulta (?q=texto). */
public class Peticion {

    private final Map<String, String> ruta;
    private final Map<String, String> consulta;

    public Peticion(Map<String, String> ruta, String queryString) {
        this.ruta = ruta;
        this.consulta = leerQuery(queryString);
    }

    private static Map<String, String> leerQuery(String query) {
        Map<String, String> mapa = new HashMap<>();
        if (query == null || query.isBlank()) {
            return mapa;
        }
        for (String par : query.split("&")) {
            int igual = par.indexOf('=');
            String clave = igual < 0 ? par : par.substring(0, igual);
            String valor = igual < 0 ? "" : par.substring(igual + 1);
            mapa.put(URLDecoder.decode(clave, StandardCharsets.UTF_8), URLDecoder.decode(valor, StandardCharsets.UTF_8));
        }
        return mapa;
    }

    /** Parámetro de la ruta como texto, por ejemplo la placa en /api/vehiculos/{placa}. */
    public String ruta(String nombre) {
        return ruta.get(nombre);
    }

    /** Parámetro de la ruta como número entero. */
    public int rutaEntero(String nombre) {
        try {
            return Integer.parseInt(ruta.get(nombre));
        } catch (NumberFormatException e) {
            throw new SolicitudInvalidaException("Parámetro con formato no válido: " + nombre);
        }
    }

    /** Parámetro de consulta (?nombre=valor); devuelve null si no llega o está vacío. */
    public String texto(String nombre) {
        String valor = consulta.get(nombre);
        return (valor == null || valor.isBlank()) ? null : valor.trim();
    }

    public Integer entero(String nombre) {
        String valor = texto(nombre);
        if (valor == null) return null;
        try {
            return Integer.valueOf(valor);
        } catch (NumberFormatException e) {
            throw new SolicitudInvalidaException("Parámetro con formato no válido: " + nombre);
        }
    }

    /** Fecha en formato AAAA-MM-DD. */
    public LocalDate fecha(String nombre) {
        String valor = texto(nombre);
        if (valor == null) return null;
        try {
            return LocalDate.parse(valor);
        } catch (DateTimeParseException e) {
            throw new SolicitudInvalidaException("Parámetro con formato no válido: " + nombre);
        }
    }
}
