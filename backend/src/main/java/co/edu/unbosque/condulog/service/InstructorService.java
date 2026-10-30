package co.edu.unbosque.condulog.service;

import co.edu.unbosque.condulog.model.instructor.InstructorResumen;
import co.edu.unbosque.condulog.repository.InstructorRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/** Reglas de negocio de los instructores. */
@Service
public class InstructorService {

    private final InstructorRepository instructorRepository;

    public InstructorService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    /** Instructores con el estado de su licencia (vencida, por vencer en 60 días o vigente) y sus categorías. */
    public List<InstructorResumen> listar() {
        LocalDate hoy = LocalDate.now();
        Map<Integer, String> categorias = Agrupador.agrupar(instructorRepository.categorias(), "id_instructor", "codigo_categoria");
        return instructorRepository.listar(hoy, hoy.plusDays(60)).stream()
                .map(i -> i.conCategorias(categorias.get(i.idInstructor())))
                .toList();
    }
}
