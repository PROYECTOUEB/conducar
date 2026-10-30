package co.edu.unbosque.conducar.model.alumno;

import java.time.LocalDate;


public record MatriculaAvance(
        Integer idMatricula,
        String codigoCategoria,
        String descripcion,
        LocalDate fechaMatricula,
        String estado,
        Double valorPactado,
        Double totalPagado,
        Integer horasTeoriaPlan,
        Integer horasTeoriaDictadas,
        Integer horasTallerPlan,
        Integer horasTallerDictadas,
        Integer horasPracticaPlan,
        Integer horasPracticaDictadas,
        String numeroCertificado) {
}
