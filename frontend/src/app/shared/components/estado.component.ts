import { Component, Input } from '@angular/core';
import { TextoPipe } from '../pipes/formato.pipes';

const OK = ['VIGENTE', 'DISPONIBLE', 'DICTADA', 'ACTIVA', 'ACTIVO', 'APROBADO', 'APTO', 'FINALIZADA', 'PAGADO'];
const AVISO = ['POR VENCER', 'EN_MANTENIMIENTO', 'PROGRAMADA', 'SIN REGISTRO', 'CON SALDO'];
const MAL = ['VENCIDO', 'VENCIDA', 'FUERA_DE_SERVICIO', 'CANCELADA', 'REPROBADO', 'NO APTO', 'INACTIVO'];

/** Etiqueta de color para un estado (verde, ámbar, rojo o gris). */
@Component({
  selector: 'app-estado',
  imports: [TextoPipe],
  template: `<span [class]="'estado ' + clase">{{ valor | texto }}</span>`,
})
export class EstadoComponent {
  @Input() valor: string | null = null;

  get clase(): string {
    const v = this.valor ?? '';
    if (OK.includes(v)) return 'e-ok';
    if (AVISO.includes(v)) return 'e-aviso';
    if (MAL.includes(v)) return 'e-mal';
    return 'e-neutro';
  }
}
