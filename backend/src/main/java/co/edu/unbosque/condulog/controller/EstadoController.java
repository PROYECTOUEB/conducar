package co.edu.unbosque.condulog.controller;

import co.edu.unbosque.condulog.model.estado.EstadoConexion;
import co.edu.unbosque.condulog.service.EstadoService;
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
