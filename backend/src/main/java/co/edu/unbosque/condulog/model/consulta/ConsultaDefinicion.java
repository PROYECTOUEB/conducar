package co.edu.unbosque.condulog.model.consulta;

/** Consulta tal como está en consultas.json. */
public record ConsultaDefinicion(
        String cat,
        String q,
        String sql,
        String exp) {
}
