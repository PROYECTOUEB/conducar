import { ClaseResumen } from './clase.model';

/** Una fila de la lista de alumnos. */
export interface AlumnoResumen {
  idAlumno: number | null;
  tipoDocumento: string | null;
  numeroDocumento: string | null;
  nombres: string | null;
  apellidos: string | null;
  correo: string | null;
  telefono: string | null;
  categoriaActiva: string | null;
  horasDictadas: number | null;
  estadoCrc: string | null;
}

/** Datos personales del alumno. */
export interface AlumnoDatos {
  idAlumno: number | null;
  tipoDocumento: string | null;
  numeroDocumento: string | null;
  nombres: string | null;
  apellidos: string | null;
  fechaNacimiento: string | null;
  telefono: string | null;
  correo: string | null;
  fechaRegistro: string | null;
  edad: number | null;
}

/** Examen médico del Centro de Reconocimiento de Conductores (CRC). */
export interface CertificadoCrc {
  nombreCrc: string | null;
  fechaExamen: string | null;
  resultado: string | null;
}

/** Matrícula del alumno con lo pagado y el avance de horas por tipo de clase. */
export interface MatriculaAvance {
  idMatricula: number | null;
  codigoCategoria: string | null;
  descripcion: string | null;
  fechaMatricula: string | null;
  estado: string | null;
  valorPactado: number | null;
  totalPagado: number | null;
  horasTeoriaPlan: number | null;
  horasTeoriaDictadas: number | null;
  horasTallerPlan: number | null;
  horasTallerDictadas: number | null;
  horasPracticaPlan: number | null;
  horasPracticaDictadas: number | null;
  numeroCertificado: string | null;
}

/** Examen teórico o práctico presentado por el alumno. */
export interface ExamenAlumno {
  idExamen: number | null;
  idMatricula: number | null;
  codigoCategoria: string | null;
  tipo: string | null;
  fecha: string | null;
  puntaje: number | null;
  resultado: string | null;
  instructor: string | null;
}

/** Pago realizado por el alumno. */
export interface PagoAlumno {
  idPago: number | null;
  idMatricula: number | null;
  fechaPago: string | null;
  valor: number | null;
  medioPago: string | null;
}

/** Respuesta del detalle de un alumno. */
export interface AlumnoDetalle {
  datos: AlumnoDatos;
  certificados: CertificadoCrc[];
  matriculas: MatriculaAvance[];
  clases: ClaseResumen[];
  examenes: ExamenAlumno[];
  pagos: PagoAlumno[];
}
