package co.edu.unbosque.conducar.model.alumno;

import co.edu.unbosque.conducar.model.clase.ClaseResumen;
import java.util.List;

/** Respuesta del detalle de un alumno. */
public record AlumnoDetalle(
        AlumnoDatos datos,
        List<CertificadoCrc> certificados,
        List<MatriculaAvance> matriculas,
        List<ClaseResumen> clases,
        List<ExamenAlumno> examenes,
        List<PagoAlumno> pagos) {
}
