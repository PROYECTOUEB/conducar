package co.edu.unbosque.conducar.controller;

import co.edu.unbosque.conducar.http.Peticion;
import co.edu.unbosque.conducar.http.Router;
import co.edu.unbosque.conducar.model.estado.EstadoConexion;
import co.edu.unbosque.conducar.service.EstadoService;

import java.sql.SQLException;

public class EstadoController {

    private final EstadoService estadoService;

    public EstadoController(EstadoService estadoService) {
        this.estadoService = estadoService;
    }

    public void registrar(Router router) {
        router.get("/api/estado", this::estado);
    }

    /** GET /api/estado: comprueba la conexión con MySQL */
    public EstadoConexion estado(Peticion p) throws SQLException {
        return estadoService.estado();
    }
}
