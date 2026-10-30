package co.edu.unbosque.condulog.controller;

import co.edu.unbosque.condulog.model.clase.ClaseResumen;
import co.edu.unbosque.condulog.model.clase.FiltroClases;
import co.edu.unbosque.condulog.service.ClaseService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clases")
public class ClaseController {

    private final ClaseService claseService;

    public ClaseController(ClaseService claseService) {
        this.claseService = claseService;
    }

    /** GET /api/clases?estado=&tipo=&instructor=&placa=&desde=AAAA-MM-DD&hasta=AAAA-MM-DD */
    @GetMapping
    public List<ClaseResumen> listar(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Integer instructor,
            @RequestParam(required = false) String placa,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return claseService.buscar(new FiltroClases(vacioANulo(estado), vacioANulo(tipo), instructor, vacioANulo(placa), desde, hasta));
    }

    private static String vacioANulo(String valor) {
        return (valor == null || valor.isBlank()) ? null : valor.trim();
    }
}
