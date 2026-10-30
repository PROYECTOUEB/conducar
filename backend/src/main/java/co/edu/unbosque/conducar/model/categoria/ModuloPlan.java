package co.edu.unbosque.conducar.model.categoria;


public record ModuloPlan(
        String codigoCategoria,
        Integer idModulo,
        String nombre,
        String tipo,
        String descripcion,
        Integer horas) {
}
