package co.edu.unbosque.conducar.service;

import co.edu.unbosque.conducar.exception.SolicitudInvalidaException;
import co.edu.unbosque.conducar.model.clase.ClaseResumen;
import co.edu.unbosque.conducar.model.clase.FiltroClases;
import co.edu.unbosque.conducar.repository.ClaseRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/** Reglas de negocio de la agenda de clases. */
@Service
public class ClaseService {

    /** Valores permitidos por los CHECK de las tablas MODULO y CLASE. */
    public static final List<String> TIPOS = List.of("TEORIA", "TALLER", "PRACTICA");
    public static final List<String> ESTADOS = List.of("PROGRAMADA", "DICTADA", "CANCELADA");

    private final ClaseRepository claseRepository;

    public ClaseService(ClaseRepository claseRepository) {
        this.claseRepository = claseRepository;
    }

    public List<ClaseResumen> buscar(FiltroClases filtro) {
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
