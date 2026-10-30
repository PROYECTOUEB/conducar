package co.edu.unbosque.condulog.service;

import co.edu.unbosque.condulog.exception.SolicitudInvalidaException;
import co.edu.unbosque.condulog.model.matricula.MatriculaResumen;
import co.edu.unbosque.condulog.repository.MatriculaRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/** Matrículas y estado de pagos. */
@Service
public class MatriculaService {

    public static final List<String> ESTADOS = List.of("ACTIVA", "FINALIZADA", "CANCELADA");

    private final MatriculaRepository matriculaRepository;

    public MatriculaService(MatriculaRepository matriculaRepository) {
        this.matriculaRepository = matriculaRepository;
    }

    public List<MatriculaResumen> listar(String estado) {
        if (estado == null || estado.isBlank()) {
            return matriculaRepository.listar(null);
        }
        if (!ESTADOS.contains(estado)) {
            throw new SolicitudInvalidaException("Estado de matrícula no válido: " + estado);
        }
        return matriculaRepository.listar(estado);
    }
}
