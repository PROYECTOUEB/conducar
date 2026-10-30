/** Matrícula con su estado de pagos y de horas. */
export interface MatriculaResumen {
  idMatricula: number | null;
  idAlumno: number | null;
  alumno: string | null;
  codigoCategoria: string | null;
  fechaMatricula: string | null;
  estado: string | null;
  valorPactado: number | null;
  totalPagado: number | null;
  saldo: number | null;
  horasDictadas: number | null;
  horasPlan: number | null;
}
