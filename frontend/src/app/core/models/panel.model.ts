import { ClaseResumen } from './clase.model';

/** Indicadores principales del panel. */
export interface Indicadores {
  alumnosActivos: number | null;
  matriculasActivas: number | null;
  clasesProgramadas: number | null;
  horasDictadas: number | null;
  vehiculosDisponibles: number | null;
  vehiculos: number | null;
  instructoresActivos: number | null;
  saldoPendiente: number | null;
  tasaAprobacion: number | null;
}

/** Situación que requiere atención (SOAT, revisión técnico-mecánica, licencias, CRC, saldos). */
export interface Alerta {
  tipo: string | null;
  sujeto: string | null;
  detalle: string | null;
  fecha: string | null;
  nivel: string | null;
  enlace: string | null;
}

/** Horas de clase dictadas por un instructor. */
export interface HorasInstructor {
  instructor: string | null;
  horas: number | null;
}

/** Matrículas por categoría de licencia. */
export interface MatriculasCategoria {
  categoria: string | null;
  matriculas: number | null;
}

/** Clases dictadas y canceladas en un mes (AAAA-MM). */
export interface ClasesPorMes {
  mes: string | null;
  dictadas: number | null;
  canceladas: number | null;
}

/** Respuesta completa del panel. */
export interface Panel {
  kpi: Indicadores;
  alertas: Alerta[];
  horasInstructor: HorasInstructor[];
  matriculasCategoria: MatriculasCategoria[];
  clasesPorMes: ClasesPorMes[];
  proximas: ClaseResumen[];
}
