package co.edu.unbosque.condulog.model.catalogo;

import co.edu.unbosque.condulog.model.instructor.InstructorOpcion;
import java.util.List;

/** Valores para llenar los filtros de la interfaz. */
public record Catalogos(
        List<InstructorOpcion> instructores,
        List<String> placas,
        List<String> tiposClase,
        List<String> estadosClase) {
}
