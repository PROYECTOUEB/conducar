package co.edu.unbosque.conducar.exception;

import java.sql.SQLException;

/** Convierte una excepción en el código HTTP y el mensaje que recibe el frontend. */
public final class ManejadorErrores {

    public record Respuesta(int estado, String mensaje) {
    }

    private ManejadorErrores() {
    }

    public static Respuesta manejar(Exception e) {
        if (e instanceof RecursoNoEncontradoException) {
            return new Respuesta(404, e.getMessage());                                   // no existe
        }
        if (e instanceof SolicitudInvalidaException) {
            return new Respuesta(400, e.getMessage());                                   // parámetro mal enviado
        }
        if (e instanceof SQLException) {
            System.err.println("Error de base de datos: " + e.getMessage());
            return new Respuesta(503, "No fue posible consultar la base de datos: " + e.getMessage());
        }
        e.printStackTrace();
        return new Respuesta(500, "Error interno del servidor: " + e.getMessage());
    }
}
