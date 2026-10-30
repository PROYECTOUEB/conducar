package co.edu.unbosque.condulog.model.instructor;

import java.time.LocalDate;

/** Instructor con su licencia, categorías y horas dictadas. */
public record InstructorResumen(
        Integer idInstructor,
        String nombre,
        String correo,
        String telefono,
        String numeroLicencia,
        LocalDate fechaVencLicencia,
        String estadoLicencia,
        String estado,
        Double horasDictadas,
        Integer clasesProgramadas,
        String categorias) {

    /** Devuelve una copia con las categorías que puede enseñar. */
    public InstructorResumen conCategorias(String lista) {
        return new InstructorResumen(idInstructor, nombre, correo, telefono, numeroLicencia, fechaVencLicencia,
                estadoLicencia, estado, horasDictadas, clasesProgramadas, lista);
    }
}
