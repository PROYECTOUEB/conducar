package co.edu.unbosque.condulog.controller;

import co.edu.unbosque.condulog.model.alumno.AlumnoDetalle;
import co.edu.unbosque.condulog.model.alumno.AlumnoResumen;
import co.edu.unbosque.condulog.service.AlumnoService;
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
