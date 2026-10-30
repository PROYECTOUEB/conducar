package co.edu.unbosque.condulog.service;

import co.edu.unbosque.condulog.model.categoria.Categoria;
import co.edu.unbosque.condulog.model.categoria.ModuloPlan;
import co.edu.unbosque.condulog.repository.CategoriaRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/** Categorías de licencia con el plan de estudios de cada curso. */
@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> categorias() {
        List<ModuloPlan> plan = categoriaRepository.plan();
        return categoriaRepository.categorias().stream()
                .map(c -> c.conModulos(plan.stream().filter(m -> m.codigoCategoria().equals(c.codigoCategoria())).toList()))
                .toList();
    }
}
