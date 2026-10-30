package co.edu.unbosque.conducar.model.alumno;

import java.time.LocalDate;


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
