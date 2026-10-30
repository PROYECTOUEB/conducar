package co.edu.unbosque.conducar.model.alumno;

import java.time.LocalDate;


public record PagoAlumno(
        Integer idPago,
        Integer idMatricula,
        LocalDate fechaPago,
        Double valor,
        String medioPago) {
}
