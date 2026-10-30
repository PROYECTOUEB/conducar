package co.edu.unbosque.condulog.model.panel;

/** Indicadores principales del panel. */
public record Indicadores(
        Integer alumnosActivos,
        Integer matriculasActivas,
        Integer clasesProgramadas,
        Double horasDictadas,
        Integer vehiculosDisponibles,
        Integer vehiculos,
        Integer instructoresActivos,
        Double saldoPendiente,
        Double tasaAprobacion) {
}
