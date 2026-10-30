package co.edu.unbosque.condulog.controller;

import co.edu.unbosque.condulog.model.catalogo.Catalogos;
import co.edu.unbosque.condulog.service.CatalogoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/catalogos")
public class CatalogoController {

    private final CatalogoService catalogoService;

    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @GetMapping
    public Catalogos catalogos() {
        return catalogoService.catalogos();
    }
}
