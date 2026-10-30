package co.edu.unbosque.conducar.controller;

import co.edu.unbosque.conducar.model.matricula.MatriculaResumen;
import co.edu.unbosque.conducar.service.MatriculaService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {

    private final MatriculaService matriculaService;

    public MatriculaController(MatriculaService matriculaService) {
        this.matriculaService = matriculaService;
    }

    /** GET /api/matriculas?estado=ACTIVA|FINALIZADA|CANCELADA */
    @GetMapping
    public List<MatriculaResumen> listar(@RequestParam(required = false) String estado) {
        return matriculaService.listar(estado);
    }
}
