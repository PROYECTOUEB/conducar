package co.edu.unbosque.condulog.model.categoria;

/** Módulo (materia) del plan de estudios de una categoría. */
public record ModuloPlan(
        String codigoCategoria,
        Integer idModulo,
        String nombre,
        String tipo,
        String descripcion,
        Integer horas) {
}
