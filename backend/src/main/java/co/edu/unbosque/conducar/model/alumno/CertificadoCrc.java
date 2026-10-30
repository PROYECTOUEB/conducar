package co.edu.unbosque.conducar.model.alumno;

import java.time.LocalDate;

/** Examen médico del Centro de Reconocimiento de Conductores (CRC). */
public record CertificadoCrc(
        String nombreCrc,
        LocalDate fechaExamen,
        String resultado) {
}
