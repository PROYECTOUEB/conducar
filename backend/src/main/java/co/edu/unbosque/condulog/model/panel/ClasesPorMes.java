package co.edu.unbosque.condulog.model.panel;

/** Clases dictadas y canceladas en un mes (AAAA-MM). */
public record ClasesPorMes(
        String mes,
        Integer dictadas,
        Integer canceladas) {
}
