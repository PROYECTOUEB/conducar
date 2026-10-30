/** Elemento del catálogo de consultas. */
export interface ConsultaItem {
  n: number | null;
  categoria: string | null;
  pregunta: string | null;
}

/** Consulta con el resultado de ejecutar su SQL. */
export interface ConsultaResultado {
  n: number | null;
  categoria: string | null;
  pregunta: string | null;
  explicacion: string | null;
  sql: string | null;
  columnas: string | null[];
  filas: unknown[][];
  total: number | null;
}
