/** Módulo (materia) del plan de estudios de una categoría. */
export interface ModuloPlan {
  codigoCategoria: string | null;
  idModulo: number | null;
  nombre: string | null;
  tipo: string | null;
  descripcion: string | null;
  horas: number | null;
}

/** Categoría de licencia con su curso y plan de estudios. */
export interface Categoria {
  codigoCategoria: string | null;
  descripcion: string | null;
  tipoServicio: string | null;
  edadMinima: number | null;
  valorCurso: number | null;
  horasTotales: number | null;
  matriculasActivas: number | null;
  matriculas: number | null;
  modulos: ModuloPlan[];
}
