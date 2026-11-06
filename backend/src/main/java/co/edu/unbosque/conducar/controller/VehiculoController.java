package co.edu.unbosque.conducar.controller;

import co.edu.unbosque.conducar.http.Peticion;
import co.edu.unbosque.conducar.http.Router;
import co.edu.unbosque.conducar.model.vehiculo.VehiculoDetalle;
import co.edu.unbosque.conducar.model.vehiculo.VehiculoResumen;
import co.edu.unbosque.conducar.service.VehiculoService;

import java.sql.SQLException;
import java.util.List;

public class VehiculoController {

    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    public void registrar(Router router) {
        router.get("/api/vehiculos", this::listar)
              .get("/api/vehiculos/{placa}", this::detalle);
    }

    /** GET /api/vehiculos */
    public List<VehiculoResumen> listar(Peticion p) throws SQLException {
        return vehiculoService.listar();
    }

    /** GET /api/vehiculos/{placa} */
    public VehiculoDetalle detalle(Peticion p) throws SQLException {
        return vehiculoService.detalle(p.ruta("placa"));
    }
}
