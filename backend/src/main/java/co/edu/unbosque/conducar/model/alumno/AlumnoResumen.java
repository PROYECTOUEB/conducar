package co.edu.unbosque.conducar.model.alumno;

import java.time.LocalDate;


public record AlumnoResumen(
        Integer idAlumno,
        String tipoDocumento,
        String numeroDocumento,
        String nombres,
        String apellidos,
        LocalDate fechaNacimiento,
        String telefono,
        String correo,
        LocalDate fechaRegistro,
        String categoriaActiva,
        Double horasDictadas,
        String estadoCrc) {
}
