package co.edu.unbosque.condulog.model.alumno;

import java.time.LocalDate;

/** Pago realizado por el alumno. */
public record PagoAlumno(
        Integer idPago,
        Integer idMatricula,
        LocalDate fechaPago,
        Double valor,
        String medioPago) {
}
