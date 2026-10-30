package co.edu.unbosque.conducar.service;

import co.edu.unbosque.conducar.model.estado.EstadoConexion;
import co.edu.unbosque.conducar.repository.EstadoRepository;
import org.springframework.stereotype.Service;

/** Verifica la conexión con MySQL. */
@Service
public class EstadoService {

    private final EstadoRepository estadoRepository;

    public EstadoService(EstadoRepository estadoRepository) {
        this.estadoRepository = estadoRepository;
    }

    public EstadoConexion estado() {
        return new EstadoConexion("ok", estadoRepository.servidor(), estadoRepository.numeroTablas(), estadoRepository.conteos());
    }
}
