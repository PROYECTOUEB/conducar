package co.edu.unbosque.conducar.controller;

import co.edu.unbosque.conducar.model.alumno.AlumnoDetalle;
import co.edu.unbosque.conducar.model.alumno.AlumnoResumen;
import co.edu.unbosque.conducar.service.AlumnoService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController {

    private final AlumnoService alumnoService;

    public AlumnoController(AlumnoService alumnoService) {
        this.alumnoService = alumnoService;
    }

    /** GET /api/alumnos?q=texto */
    @GetMapping
    public List<AlumnoResumen> listar(@RequestParam(required = false) String q) {
        return alumnoService.buscar(q);
    }

    @GetMapping("/{id}")
    public AlumnoDetalle detalle(@PathVariable int id) {
        return alumnoService.detalle(id);
    }
}
