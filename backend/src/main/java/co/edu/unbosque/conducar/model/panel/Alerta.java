package co.edu.unbosque.conducar.model.panel;

import java.time.LocalDate;


public record Alerta(
        String tipo,
        String sujeto,
        String detalle,
        LocalDate fecha,
        String nivel,
        String enlace) {
}
