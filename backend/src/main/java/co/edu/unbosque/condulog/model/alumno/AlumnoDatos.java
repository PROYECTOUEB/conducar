package co.edu.unbosque.condulog.model.alumno;

import java.time.LocalDate;

/** Datos personales del alumno. */
public record AlumnoDatos(
        Integer idAlumno,
        String tipoDocumento,
        String numeroDocumento,
        String nombres,
        String apellidos,
        LocalDate fechaNacimiento,
        String telefono,
        String correo,
        LocalDate fechaRegistro,
        Integer edad) {

    /** Devuelve una copia con la edad calculada a la fecha indicada. */
    public AlumnoDatos conEdad(LocalDate hoy) {
        Integer anios = fechaNacimiento == null ? null : java.time.Period.between(fechaNacimiento, hoy).getYears();
        return new AlumnoDatos(idAlumno, tipoDocumento, numeroDocumento, nombres, apellidos, fechaNacimiento,
                telefono, correo, fechaRegistro, anios);
    }
}
