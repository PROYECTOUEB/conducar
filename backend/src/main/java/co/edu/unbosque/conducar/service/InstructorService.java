package co.edu.unbosque.conducar.service;

import co.edu.unbosque.conducar.model.instructor.InstructorResumen;
import co.edu.unbosque.conducar.repository.InstructorRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InstructorService {

    private final InstructorRepository instructorRepository;

    public InstructorService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    /** Instructores con sus categorías habilitadas unidas en un texto ("A2, B1, C1"). */
    public List<InstructorResumen> listar() throws SQLException {
        Map<Integer, String> categorias = new LinkedHashMap<>();
        for (String[] fila : instructorRepository.categorias()) {
            categorias.merge(Integer.valueOf(fila[0]), fila[1], (anterior, nueva) -> anterior + ", " + nueva);
        }
        LocalDate hoy = LocalDate.now();
        List<InstructorResumen> resultado = new ArrayList<>();
        for (InstructorResumen i : instructorRepository.listar(hoy, hoy.plusDays(60))) {
            resultado.add(i.conCategorias(categorias.get(i.idInstructor())));
        }
        return resultado;
    }
}
