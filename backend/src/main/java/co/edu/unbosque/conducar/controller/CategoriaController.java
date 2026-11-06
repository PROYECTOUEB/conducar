package co.edu.unbosque.conducar.controller;

import co.edu.unbosque.conducar.http.Peticion;
import co.edu.unbosque.conducar.http.Router;
import co.edu.unbosque.conducar.model.categoria.Categoria;
import co.edu.unbosque.conducar.service.CategoriaService;

import java.sql.SQLException;
import java.util.List;

public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    public void registrar(Router router) {
        router.get("/api/categorias", this::categorias);
    }

    /** GET /api/categorias */
    public List<Categoria> categorias(Peticion p) throws SQLException {
        return categoriaService.categorias();
    }
}
