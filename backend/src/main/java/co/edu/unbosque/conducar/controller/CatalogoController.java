package co.edu.unbosque.conducar.controller;

import co.edu.unbosque.conducar.model.catalogo.Catalogos;
import co.edu.unbosque.conducar.service.CatalogoService;
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
