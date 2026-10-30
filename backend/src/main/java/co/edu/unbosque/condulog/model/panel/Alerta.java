package co.edu.unbosque.condulog.model.panel;

import java.time.LocalDate;

/** Situación que requiere atención (SOAT, revisión técnico-mecánica, licencias, CRC, saldos). */
public record Alerta(
        String tipo,
        String sujeto,
        String detalle,
        LocalDate fecha,
        String nivel,
        String enlace) {
}
