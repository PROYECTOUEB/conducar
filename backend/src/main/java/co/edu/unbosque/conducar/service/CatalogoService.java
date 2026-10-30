package co.edu.unbosque.conducar.service;

import co.edu.unbosque.conducar.model.catalogo.Catalogos;
import co.edu.unbosque.conducar.repository.InstructorRepository;
import co.edu.unbosque.conducar.repository.VehiculoRepository;
import org.springframework.stereotype.Service;


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
