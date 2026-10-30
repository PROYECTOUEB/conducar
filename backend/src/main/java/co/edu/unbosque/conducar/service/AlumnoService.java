package co.edu.unbosque.conducar.service;

import co.edu.unbosque.conducar.exception.RecursoNoEncontradoException;
import co.edu.unbosque.conducar.model.alumno.AlumnoDatos;
import co.edu.unbosque.conducar.model.alumno.AlumnoDetalle;
import co.edu.unbosque.conducar.model.alumno.AlumnoResumen;
import co.edu.unbosque.conducar.repository.AlumnoRepository;
import co.edu.unbosque.conducar.repository.ClaseRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

/** Reglas de negocio de los alumnos. */
@Service
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final ClaseRepository claseRepository;

    public AlumnoService(AlumnoRepository alumnoRepository, ClaseRepository claseRepository) {
        this.alumnoRepository = alumnoRepository;
        this.claseRepository = claseRepository;
    }

    public List<AlumnoResumen> buscar(String texto) {
        String filtro = (texto == null || texto.isBlank()) ? null : texto.trim();
        return alumnoRepository.buscar(filtro);
    }

    public AlumnoDetalle detalle(int id) {
        AlumnoDatos datos = alumnoRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el alumno " + id))
                .conEdad(LocalDate.now());
        return new AlumnoDetalle(
                datos,
                alumnoRepository.certificados(id),
                alumnoRepository.matriculas(id),
                claseRepository.porAlumno(id),
                alumnoRepository.examenes(id),
                alumnoRepository.pagos(id));
    }
}
