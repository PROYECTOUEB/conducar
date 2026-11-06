package co.edu.unbosque.conducar.controller;

import co.edu.unbosque.conducar.http.Peticion;
import co.edu.unbosque.conducar.http.Router;
import co.edu.unbosque.conducar.model.alumno.AlumnoDetalle;
import co.edu.unbosque.conducar.model.alumno.AlumnoResumen;
import co.edu.unbosque.conducar.service.AlumnoService;

import java.sql.SQLException;
import java.util.List;

public class AlumnoController {

    private final AlumnoService alumnoService;

    public AlumnoController(AlumnoService alumnoService) {
        this.alumnoService = alumnoService;
    }

    public void registrar(Router router) {
        router.get("/api/alumnos", this::listar)
              .get("/api/alumnos/{id}", this::detalle);
    }

    /** GET /api/alumnos?q=texto */
    public List<AlumnoResumen> listar(Peticion p) throws SQLException {
        return alumnoService.buscar(p.texto("q"));
    }

    /** GET /api/alumnos/{id} */
    public AlumnoDetalle detalle(Peticion p) throws SQLException {
        return alumnoService.detalle(p.rutaEntero("id"));
    }
}
