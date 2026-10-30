package co.edu.unbosque.condulog.exception;

/** Se lanza cuando el registro solicitado no existe (respuesta 404). */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
