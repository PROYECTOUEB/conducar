package co.edu.unbosque.condulog.service;

import co.edu.unbosque.condulog.model.catalogo.Catalogos;
import co.edu.unbosque.condulog.repository.InstructorRepository;
import co.edu.unbosque.condulog.repository.VehiculoRepository;
import org.springframework.stereotype.Service;

/** Valores para los filtros de la interfaz. */
@Service
public class CatalogoService {

    private final InstructorRepository instructorRepository;
    private final VehiculoRepository vehiculoRepository;

    public CatalogoService(InstructorRepository instructorRepository, VehiculoRepository vehiculoRepository) {
        this.instructorRepository = instructorRepository;
        this.vehiculoRepository = vehiculoRepository;
    }

    public Catalogos catalogos() {
        return new Catalogos(instructorRepository.opciones(), vehiculoRepository.placas(), ClaseService.TIPOS, ClaseService.ESTADOS);
    }
}
