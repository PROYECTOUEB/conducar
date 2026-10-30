package co.edu.unbosque.condulog.service;

import co.edu.unbosque.condulog.exception.RecursoNoEncontradoException;
import co.edu.unbosque.condulog.model.vehiculo.VehiculoDetalle;
import co.edu.unbosque.condulog.model.vehiculo.VehiculoResumen;
import co.edu.unbosque.condulog.repository.ClaseRepository;
import co.edu.unbosque.condulog.repository.VehiculoRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

/** Vehículos de enseñanza. */
@Service
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final ClaseRepository claseRepository;

    public VehiculoService(VehiculoRepository vehiculoRepository, ClaseRepository claseRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.claseRepository = claseRepository;
    }

    public List<VehiculoResumen> listar() {
        return vehiculoRepository.listar(LocalDate.now());
    }

    public VehiculoDetalle detalle(String placa) {
        VehiculoResumen datos = vehiculoRepository.buscarPorPlaca(placa, LocalDate.now())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el vehículo " + placa));
        return new VehiculoDetalle(datos, claseRepository.porVehiculo(placa, 30));
    }
}
