import { Component } from '@angular/core';

/** Colores de las tiras de clase por tipo. */
@Component({
  selector: 'app-leyenda',
  template: `
    <p class="leyenda" aria-label="Colores por tipo de clase">
      <span class="TEORIA">Teoría</span><span class="TALLER">Taller</span><span class="PRACTICA">Práctica</span>
    </p>`,
})
export class LeyendaComponent {}
