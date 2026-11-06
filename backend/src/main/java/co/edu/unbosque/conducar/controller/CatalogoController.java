package co.edu.unbosque.conducar.controller;

import co.edu.unbosque.conducar.http.Peticion;
import co.edu.unbosque.conducar.http.Router;
import co.edu.unbosque.conducar.model.catalogo.Catalogos;
import co.edu.unbosque.conducar.service.CatalogoService;

import java.sql.SQLException;

public class CatalogoController {

    private final CatalogoService catalogoService;

    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    public void registrar(Router router) {
        router.get("/api/catalogos", this::catalogos);
    }

    /** GET /api/catalogos */
    public Catalogos catalogos(Peticion p) throws SQLException {
        return catalogoService.catalogos();
    }
}
