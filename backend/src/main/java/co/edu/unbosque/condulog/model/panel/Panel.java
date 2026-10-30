package co.edu.unbosque.condulog.model.panel;

import co.edu.unbosque.condulog.model.clase.ClaseResumen;
import java.util.List;

/** Respuesta completa del panel. */
public record Panel(
        Indicadores kpi,
        List<Alerta> alertas,
        List<HorasInstructor> horasInstructor,
        List<MatriculasCategoria> matriculasCategoria,
        List<ClasesPorMes> clasesPorMes,
        List<ClaseResumen> proximas) {
}
