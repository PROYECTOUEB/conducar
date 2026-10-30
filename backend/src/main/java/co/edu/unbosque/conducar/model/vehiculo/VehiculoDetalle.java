package co.edu.unbosque.conducar.model.vehiculo;

import co.edu.unbosque.conducar.model.clase.ClaseResumen;
import java.util.List;


public record VehiculoDetalle(
        VehiculoResumen datos,
        List<ClaseResumen> clases) {
}
