package co.edu.unbosque.conducar.model.alumno;

/** Una fila de la lista de alumnos. */
public record AlumnoResumen(
        Integer idAlumno,
        String tipoDocumento,
        String numeroDocumento,
        String nombres,
        String apellidos,
        String correo,
        String telefono,
        String categoriaActiva,
        Double horasDictadas,
        String estadoCrc) {
}
