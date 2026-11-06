package co.edu.unbosque.conducar.service;

import co.edu.unbosque.conducar.model.estado.EstadoConexion;
import co.edu.unbosque.conducar.repository.EstadoRepository;

import java.sql.SQLException;

public class EstadoService {

    private final EstadoRepository estadoRepository;

    public EstadoService(EstadoRepository estadoRepository) {
        this.estadoRepository = estadoRepository;
    }

    public EstadoConexion estado() throws SQLException {
        return new EstadoConexion("ok", estadoRepository.servidor(), estadoRepository.numeroTablas(), estadoRepository.conteos());
    }
}
