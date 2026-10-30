package co.edu.unbosque.conducar.service;

import co.edu.unbosque.conducar.model.instructor.InstructorResumen;
import co.edu.unbosque.conducar.repository.InstructorRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;


@Service
public class InstructorService {

    private final InstructorRepository instructorRepository;

    public InstructorService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    
    public List<InstructorResumen> listar() {
        LocalDate hoy = LocalDate.now();
        Map<Integer, String> categorias = Agrupador.agrupar(instructorRepository.categorias(), "id_instructor", "codigo_categoria");
        return instructorRepository.listar(hoy, hoy.plusDays(60)).stream()
                .map(i -> i.conCategorias(categorias.get(i.idInstructor())))
                .toList();
    }
}
