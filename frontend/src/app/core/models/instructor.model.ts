/** Instructor con su licencia, categorías y horas dictadas. */
export interface InstructorResumen {
  idInstructor: number | null;
  numeroDocumento: string | null;
  nombres: string | null;
  apellidos: string | null;
  nombre: string | null;
  telefono: string | null;
  correo: string | null;
  numeroLicencia: string | null;
  fechaVencLicencia: string | null;
  estadoLicencia: string | null;
  estado: string | null;
  horasDictadas: number | null;
  clasesProgramadas: number | null;
  categorias: string | null;
}

/** Instructor para llenar listas de selección. */
export interface InstructorOpcion {
  idInstructor: number | null;
  nombre: string | null;
}
