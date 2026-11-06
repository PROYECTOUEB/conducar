package co.edu.unbosque.conducar.service;

import co.edu.unbosque.conducar.exception.SolicitudInvalidaException;
import co.edu.unbosque.conducar.model.clase.ClaseResumen;
import co.edu.unbosque.conducar.model.clase.FiltroClases;
import co.edu.unbosque.conducar.repository.ClaseRepository;

import java.sql.SQLException;
import java.util.List;

public class ClaseService {

    /** Valores permitidos (los mismos CHECK de la base de datos). */
    public static final List<String> TIPOS = List.of("TEORIA", "TALLER", "PRACTICA");
    public static final List<String> ESTADOS = List.of("PROGRAMADA", "DICTADA", "CANCELADA");

    private final ClaseRepository claseRepository;

    public ClaseService(ClaseRepository claseRepository) {
        this.claseRepository = claseRepository;
    }

    /** Valida los filtros antes de consultar. */
    public List<ClaseResumen> buscar(FiltroClases filtro) throws SQLException {
        if (filtro.estado() != null && !ESTADOS.contains(filtro.estado())) {
            throw new SolicitudInvalidaException("Estado de clase no válido: " + filtro.estado());
        }
        if (filtro.tipo() != null && !TIPOS.contains(filtro.tipo())) {
            throw new SolicitudInvalidaException("Tipo de clase no válido: " + filtro.tipo());
        }
        if (filtro.desde() != null && filtro.hasta() != null && filtro.desde().isAfter(filtro.hasta())) {
            throw new SolicitudInvalidaException("La fecha inicial es posterior a la fecha final");
        }
        return claseRepository.buscar(filtro);
    }
}
