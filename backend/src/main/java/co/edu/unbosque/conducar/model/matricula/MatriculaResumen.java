package co.edu.unbosque.conducar.model.matricula;

import java.time.LocalDate;


public record MatriculaResumen(
        Integer idMatricula,
        Integer idAlumno,
        String alumno,
        String codigoCategoria,
        LocalDate fechaMatricula,
        String estado,
        Double valorPactado,
        Double totalPagado,
        Double saldo,
        Integer horasDictadas,
        Integer horasPlan) {
}
