package co.edu.unbosque.conducar.service;

import co.edu.unbosque.conducar.model.categoria.Categoria;
import co.edu.unbosque.conducar.model.categoria.ModuloPlan;
import co.edu.unbosque.conducar.repository.CategoriaRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    /** Cada categoría con los módulos de su plan de curso. */
    public List<Categoria> categorias() throws SQLException {
        List<ModuloPlan> plan = categoriaRepository.plan();
        List<Categoria> resultado = new ArrayList<>();
        for (Categoria c : categoriaRepository.categorias()) {
            List<ModuloPlan> modulos = new ArrayList<>();
            for (ModuloPlan m : plan) {
                if (m.codigoCategoria().equals(c.codigoCategoria())) {
                    modulos.add(m);
                }
            }
            resultado.add(c.conModulos(modulos));
        }
        return resultado;
    }
}
