package co.edu.unbosque.conducar.controller;

import co.edu.unbosque.conducar.model.vehiculo.VehiculoDetalle;
import co.edu.unbosque.conducar.model.vehiculo.VehiculoResumen;
import co.edu.unbosque.conducar.service.VehiculoService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @GetMapping
    public List<VehiculoResumen> listar() {
        return vehiculoService.listar();
    }

    @GetMapping("/{placa}")
    public VehiculoDetalle detalle(@PathVariable String placa) {
        return vehiculoService.detalle(placa);
    }
}
