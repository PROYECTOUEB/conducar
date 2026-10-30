package co.edu.unbosque.conducar.model.vehiculo;

import java.time.LocalDate;


public record VehiculoResumen(
        String placa,
        String codigoCategoria,
        String tipoVehiculo,
        String marca,
        String linea,
        Integer modeloAnio,
        String transmision,
        String estado,
        LocalDate fechaVencSoat,
        String estadoSoat,
        LocalDate fechaVencRtm,
        String estadoRtm,
        Double horasUso,
        Integer clasesProgramadas) {
}
