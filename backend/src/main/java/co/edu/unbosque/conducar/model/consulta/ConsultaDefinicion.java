package co.edu.unbosque.conducar.model.consulta;

/** Consulta tal como está en consultas.json. */
public record ConsultaDefinicion(
        String cat,
        String q,
        String sql,
        String exp) {
}
