package co.edu.unbosque.condulog.model.categoria;

import java.util.List;

/** Categoría de licencia con su curso y plan de estudios. */
public record Categoria(
        String codigoCategoria,
        String descripcion,
        String tipoServicio,
        Integer edadMinima,
        Double valorCurso,
        Integer horasTotales,
        Integer matriculasActivas,
        Integer matriculas,
        List<ModuloPlan> modulos) {

    /** Devuelve una copia con los módulos del plan de estudios. */
    public Categoria conModulos(List<ModuloPlan> plan) {
        return new Categoria(codigoCategoria, descripcion, tipoServicio, edadMinima, valorCurso, horasTotales,
                matriculasActivas, matriculas, plan);
    }
}
