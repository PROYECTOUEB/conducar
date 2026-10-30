package co.edu.unbosque.conducar.exception;

/** Se lanza cuando un parámetro de la solicitud no es válido (respuesta 400). */
public class SolicitudInvalidaException extends RuntimeException {

    public SolicitudInvalidaException(String mensaje) {
        super(mensaje);
    }
}
