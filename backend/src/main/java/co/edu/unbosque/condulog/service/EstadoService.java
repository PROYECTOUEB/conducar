package co.edu.unbosque.condulog.service;

import co.edu.unbosque.condulog.model.estado.EstadoConexion;
import co.edu.unbosque.condulog.repository.EstadoRepository;
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
