package co.edu.unbosque.conducar.exception;

import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/** Convierte las excepciones en respuestas JSON con un mensaje claro para la interfaz. */
@RestControllerAdvice
public class ManejadorErrores {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> noEncontrado(RecursoNoEncontradoException e) {
        return respuesta(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler({SolicitudInvalidaException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Map<String, String>> invalida(RuntimeException e) {
        String mensaje = e instanceof SolicitudInvalidaException ? e.getMessage() : "Parámetro con formato no válido";
        return respuesta(HttpStatus.BAD_REQUEST, mensaje);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, String>> baseDatos(DataAccessException e) {
        Throwable causa = e.getMostSpecificCause();
        return respuesta(HttpStatus.SERVICE_UNAVAILABLE,
                "No fue posible consultar la base de datos: " + causa.getMessage());
    }

    private ResponseEntity<Map<String, String>> respuesta(HttpStatus estado, String mensaje) {
        return ResponseEntity.status(estado).body(Map.of("mensaje", mensaje == null ? estado.getReasonPhrase() : mensaje));
    }
}
