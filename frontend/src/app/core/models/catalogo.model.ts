import { InstructorOpcion } from './instructor.model';

/** Valores para llenar los filtros de la interfaz. */
export interface Catalogos {
  instructores: InstructorOpcion[];
  placas: string | null[];
  tiposClase: string | null[];
  estadosClase: string | null[];
}
