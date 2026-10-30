package co.edu.unbosque.conducar.model.alumno;

import java.time.LocalDate;


public record CertificadoCrc(
        String nombreCrc,
        LocalDate fechaExamen,
        String resultado) {
}
