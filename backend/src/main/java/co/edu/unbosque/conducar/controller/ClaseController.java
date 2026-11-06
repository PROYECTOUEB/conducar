package co.edu.unbosque.conducar.controller;

import co.edu.unbosque.conducar.http.Peticion;
import co.edu.unbosque.conducar.http.Router;
import co.edu.unbosque.conducar.model.clase.ClaseResumen;
import co.edu.unbosque.conducar.model.clase.FiltroClases;
import co.edu.unbosque.conducar.service.ClaseService;

import java.sql.SQLException;
import java.util.List;

public class ClaseController {

    private final ClaseService claseService;

    public ClaseController(ClaseService claseService) {
        this.claseService = claseService;
    }

    public void registrar(Router router) {
        router.get("/api/clases", this::listar);
    }

    /** GET /api/clases?estado=&tipo=&instructor=&placa=&desde=AAAA-MM-DD&hasta=AAAA-MM-DD */
    public List<ClaseResumen> listar(Peticion p) throws SQLException {
        FiltroClases filtro = new FiltroClases(p.texto("estado"), p.texto("tipo"), p.entero("instructor"),
                p.texto("placa"), p.fecha("desde"), p.fecha("hasta"));
        return claseService.buscar(filtro);
    }
}
