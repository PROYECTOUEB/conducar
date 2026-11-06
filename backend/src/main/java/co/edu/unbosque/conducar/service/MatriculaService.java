package co.edu.unbosque.conducar.service;

import co.edu.unbosque.conducar.exception.SolicitudInvalidaException;
import co.edu.unbosque.conducar.model.matricula.MatriculaResumen;
import co.edu.unbosque.conducar.repository.MatriculaRepository;

import java.sql.SQLException;
import java.util.List;

public class MatriculaService {

    public static final List<String> ESTADOS = List.of("ACTIVA", "FINALIZADA", "CANCELADA");

    private final MatriculaRepository matriculaRepository;

    public MatriculaService(MatriculaRepository matriculaRepository) {
        this.matriculaRepository = matriculaRepository;
    }

    public List<MatriculaResumen> listar(String estado) throws SQLException {
        if (estado == null || estado.isBlank()) {
            return matriculaRepository.listar(null);
        }
        if (!ESTADOS.contains(estado)) {
            throw new SolicitudInvalidaException("Estado de matrícula no válido: " + estado);
        }
        return matriculaRepository.listar(estado);
    }
}
