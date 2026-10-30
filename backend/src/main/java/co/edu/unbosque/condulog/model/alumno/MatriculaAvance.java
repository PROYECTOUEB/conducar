package co.edu.unbosque.condulog.model.alumno;

import java.time.LocalDate;

/** Matrícula del alumno con lo pagado y el avance de horas por tipo de clase. */
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
