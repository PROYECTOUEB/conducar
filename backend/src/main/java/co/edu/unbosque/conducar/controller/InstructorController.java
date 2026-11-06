package co.edu.unbosque.conducar.controller;

import co.edu.unbosque.conducar.http.Peticion;
import co.edu.unbosque.conducar.http.Router;
import co.edu.unbosque.conducar.model.instructor.InstructorResumen;
import co.edu.unbosque.conducar.service.InstructorService;

import java.sql.SQLException;
import java.util.List;

public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    public void registrar(Router router) {
        router.get("/api/instructores", this::listar);
    }

    /** GET /api/instructores */
    public List<InstructorResumen> listar(Peticion p) throws SQLException {
        return instructorService.listar();
    }
}
