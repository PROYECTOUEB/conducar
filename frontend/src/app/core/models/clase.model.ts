/** Una clase (teoría, taller o práctica) de un alumno. */
export interface ClaseResumen {
  idClase: number | null;
  fecha: string | null;
  horaInicio: string | null;
  horas: number | null;
  estado: string | null;
  tipo: string | null;
  modulo: string | null;
  codigoCategoria: string | null;
  idMatricula: number | null;
  idAlumno: number | null;
  alumno: string | null;
  idInstructor: number | null;
  instructor: string | null;
  placa: string | null;
}
