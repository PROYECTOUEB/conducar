package co.edu.unbosque.conducar.model.catalogo;

import co.edu.unbosque.conducar.model.instructor.InstructorOpcion;
import java.util.List;

/** Valores para llenar los filtros de la interfaz. */
public record Catalogos(
        List<InstructorOpcion> instructores,
        List<String> placas,
        List<String> tiposClase,
        List<String> estadosClase) {
}
