package co.edu.unbosque.condulog.controller;

import co.edu.unbosque.condulog.model.categoria.Categoria;
import co.edu.unbosque.condulog.service.CategoriaService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public List<Categoria> categorias() {
        return categoriaService.categorias();
    }
}
