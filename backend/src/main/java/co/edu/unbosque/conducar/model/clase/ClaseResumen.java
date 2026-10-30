package co.edu.unbosque.conducar.model.clase;

import java.time.LocalDate;
import java.time.LocalTime;

/** Una clase (teoría, taller o práctica) de un alumno. */
public record ClaseResumen(
        Integer idClase,
        LocalDate fecha,
        LocalTime horaInicio,
        Integer horas,
        String estado,
        Integer idModulo,
        String tipo,
        String modulo,
        String codigoCategoria,
        Integer idMatricula,
        Integer idAlumno,
        String alumno,
        Integer idInstructor,
        String instructor,
        String placa) {
}
