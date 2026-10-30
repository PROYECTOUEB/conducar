import { ClaseResumen } from './clase.model';

/** Vehículo de enseñanza con el estado de su SOAT y revisión técnico-mecánica. */
export interface VehiculoResumen {
  placa: string | null;
  codigoCategoria: string | null;
  tipoVehiculo: string | null;
  marca: string | null;
  linea: string | null;
  modeloAnio: number | null;
  transmision: string | null;
  estado: string | null;
  fechaVencSoat: string | null;
  estadoSoat: string | null;
  fechaVencRtm: string | null;
  estadoRtm: string | null;
  horasUso: number | null;
  clasesProgramadas: number | null;
}

/** Respuesta del detalle de un vehículo. */
export interface VehiculoDetalle {
  datos: VehiculoResumen;
  clases: ClaseResumen[];
}
