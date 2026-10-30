package co.edu.unbosque.condulog.model.alumno;

import java.time.LocalDate;

/** Examen teórico o práctico presentado por el alumno. */
public record ExamenAlumno(
        Integer idExamen,
        Integer idMatricula,
        String codigoCategoria,
        String tipo,
        LocalDate fecha,
        Double puntaje,
        String resultado,
        String instructor) {
}
