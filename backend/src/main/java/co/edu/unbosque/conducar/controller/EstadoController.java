package co.edu.unbosque.conducar.controller;

import co.edu.unbosque.conducar.model.estado.EstadoConexion;
import co.edu.unbosque.conducar.service.EstadoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/estado")
public class EstadoController {

    private final EstadoService estadoService;

    public EstadoController(EstadoService estadoService) {
        this.estadoService = estadoService;
    }

    @GetMapping
    public EstadoConexion estado() {
        return estadoService.estado();
    }
}
