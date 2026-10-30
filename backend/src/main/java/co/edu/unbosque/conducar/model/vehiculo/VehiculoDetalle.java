package co.edu.unbosque.conducar.model.vehiculo;

import co.edu.unbosque.conducar.model.clase.ClaseResumen;
import java.util.List;

/** Respuesta del detalle de un vehículo. */
public record VehiculoDetalle(
        VehiculoResumen datos,
        List<ClaseResumen> clases) {
}
