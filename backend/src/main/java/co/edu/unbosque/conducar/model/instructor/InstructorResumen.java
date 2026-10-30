package co.edu.unbosque.conducar.model.instructor;

import java.time.LocalDate;

/** Instructor con su licencia, categorías y horas dictadas. */
public record InstructorResumen(
        Integer idInstructor,
        String numeroDocumento,
        String nombres,
        String apellidos,
        String nombre,
        String telefono,
        String correo,
        String numeroLicencia,
        LocalDate fechaVencLicencia,
        String estadoLicencia,
        String estado,
        Double horasDictadas,
        Integer clasesProgramadas,
        String categorias) {

    /** Devuelve una copia con las categorías que puede enseñar. */
    public InstructorResumen conCategorias(String lista) {
        return new InstructorResumen(idInstructor, numeroDocumento, nombres, apellidos, nombre, telefono, correo, numeroLicencia,
                fechaVencLicencia, estadoLicencia, estado, horasDictadas, clasesProgramadas, lista);
    }
}
