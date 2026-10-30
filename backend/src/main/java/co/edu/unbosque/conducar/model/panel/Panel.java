package co.edu.unbosque.conducar.model.panel;

import co.edu.unbosque.conducar.model.clase.ClaseResumen;
import java.util.List;


public record Panel(
        Indicadores kpi,
        List<Alerta> alertas,
        List<HorasInstructor> horasInstructor,
        List<MatriculasCategoria> matriculasCategoria,
        List<ClasesPorMes> clasesPorMes,
        List<ClaseResumen> proximas) {
}
