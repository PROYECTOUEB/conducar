package co.edu.unbosque.condulog.model.vehiculo;

import co.edu.unbosque.condulog.model.clase.ClaseResumen;
import java.util.List;

/** Respuesta del detalle de un vehículo. */
public record VehiculoDetalle(
        VehiculoResumen datos,
        List<ClaseResumen> clases) {
}
