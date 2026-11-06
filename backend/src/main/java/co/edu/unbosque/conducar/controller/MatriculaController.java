package co.edu.unbosque.conducar.controller;

import co.edu.unbosque.conducar.http.Peticion;
import co.edu.unbosque.conducar.http.Router;
import co.edu.unbosque.conducar.model.matricula.MatriculaResumen;
import co.edu.unbosque.conducar.service.MatriculaService;

import java.sql.SQLException;
import java.util.List;

public class MatriculaController {

    private final MatriculaService matriculaService;

    public MatriculaController(MatriculaService matriculaService) {
        this.matriculaService = matriculaService;
    }

    public void registrar(Router router) {
        router.get("/api/matriculas", this::listar);
    }

    /** GET /api/matriculas?estado=ACTIVA|FINALIZADA|CANCELADA */
    public List<MatriculaResumen> listar(Peticion p) throws SQLException {
        return matriculaService.listar(p.texto("estado"));
    }
}
