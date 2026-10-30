import { Component, Input } from '@angular/core';
import { NumeroPipe } from '../pipes/formato.pipes';

export interface Barra {
  etiqueta: string;
  valor: number;
}

/** Gráfico de barras horizontales sencillo (sin librerías). */
@Component({
  selector: 'app-barras',
  imports: [NumeroPipe],
  template: `
    <div class="barras">
      @for (b of items; track b.etiqueta) {
        <div class="barra">
          <span>{{ b.etiqueta }}</span>
          <div class="pista"><div class="relleno" [style.width.%]="porcentaje(b.valor)"></div></div>
          <span class="cifra">{{ b.valor | numero: decimales }}{{ sufijo }}</span>
        </div>
      }
    </div>`,
})
export class BarrasComponent {
  @Input() items: Barra[] = [];
  @Input() sufijo = '';
  @Input() decimales = 0;

  porcentaje(valor: number): number {
    const max = Math.max(1, ...this.items.map((b) => b.valor || 0));
    return (100 * (valor || 0)) / max;
  }
}
