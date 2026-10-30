package co.edu.unbosque.conducar.controller;

import co.edu.unbosque.conducar.model.consulta.ConsultaItem;
import co.edu.unbosque.conducar.model.consulta.ConsultaResultado;
import co.edu.unbosque.conducar.service.ConsultaService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @GetMapping
    public List<ConsultaItem> catalogo() {
        return consultaService.catalogo();
    }

    @GetMapping("/{n}")
    public ConsultaResultado ejecutar(@PathVariable int n) {
        return consultaService.ejecutar(n);
    }
}
