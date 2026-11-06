/** Funciones de formato usadas por los pipes y los componentes. */
import { HttpErrorResponse } from '@angular/common/http';

const MESES = ['ene', 'feb', 'mar', 'abr', 'may', 'jun', 'jul', 'ago', 'sep', 'oct', 'nov', 'dic'];
const TILDES: Record<string, string> = {
  teoria: 'teoría', practica: 'práctica', mecanica: 'mecánica', automatica: 'automática', automovil: 'automóvil',
  publico: 'público', teorico: 'teórico', practico: 'práctico', camion: 'camión',
};

export const TIPOS_CLASE: Record<string, string> = {
  TEORIA: 'Teoría', TALLER: 'Taller', PRACTICA: 'Práctica',
};

/** '2026-10-29' -> '29 oct 2026' */
export function fecha(valor: string | null | undefined): string {
  if (!valor) {
    return '—';
  }
  const [a, m, d] = valor.slice(0, 10).split('-');
  return `${Number(d)} ${MESES[Number(m) - 1]} ${a}`;
}

/** '08:30:00' -> '08:30' */
export function hora(valor: string | null | undefined): string {
  return valor ? valor.slice(0, 5) : '—';
}

export function numero(valor: number | string | null | undefined, decimales = 1): string {
  if (valor === null || valor === undefined || valor === '') {
    return '—';
  }
  return Number(valor).toLocaleString('es-CO', { minimumFractionDigits: decimales, maximumFractionDigits: decimales });
}

export function pesos(valor: number | null | undefined): string {
  if (valor === null || valor === undefined) {
    return '—';
  }
  return Number(valor).toLocaleString('es-CO', { style: 'currency', currency: 'COP', maximumFractionDigits: 0 });
}

/** 'EN_MANTENIMIENTO' -> 'En mantenimiento', 'NAVEGACION' -> 'Navegación' */
export function texto(valor: string | null | undefined): string {
  if (!valor) {
    return '—';
  }
  const t = valor.replace(/_/g, ' ').toLowerCase().replace(/[a-záéíóúñ]+/g, (w) => TILDES[w] ?? w);
  return t.charAt(0).toUpperCase() + t.slice(1);
}

/** Mensaje legible de un error HTTP (el backend envía { mensaje }). */
export function mensajeError(e: unknown): string {
  if (e instanceof HttpErrorResponse) {
    if (e.error && typeof e.error === 'object' && 'mensaje' in e.error) {
      return String(e.error.mensaje);
    }
    if (e.status === 0 || e.status === 504 || e.status === 502) {
      return 'No hay conexión con el backend. Verifique que el backend (Java) esté en ejecución en el puerto 8080.';
    }
    return `Error ${e.status} al consultar ${e.url ?? 'la API'}`;
  }
  return String(e);
}
