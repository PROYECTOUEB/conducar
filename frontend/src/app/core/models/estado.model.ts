/** Datos del servidor MySQL al que está conectada la aplicación. */
export interface ServidorInfo {
  version: string | null;
  baseDatos: string | null;
  usuario: string | null;
  servidor: string | null;
  puerto: number | null;
  fechaServidor: string | null;
  soloLectura: boolean;
}

/** Estado de la conexión y conteo de registros. */
export interface EstadoConexion {
  conexion: string | null;
  servidor: ServidorInfo;
  tablas: number | null;
  conteos: Record<string, number>;
}
