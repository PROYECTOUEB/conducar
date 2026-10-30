package co.edu.unbosque.conducar.model.alumno;

import java.time.LocalDate;


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

   
    public AlumnoDatos conEdad(LocalDate hoy) {
        Integer anios = fechaNacimiento == null ? null : java.time.Period.between(fechaNacimiento, hoy).getYears();
        return new AlumnoDatos(idAlumno, tipoDocumento, numeroDocumento, nombres, apellidos, fechaNacimiento,
                telefono, correo, fechaRegistro, anios);
    }
}
