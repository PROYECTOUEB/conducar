import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-no-encontrado',
  imports: [RouterLink],
  template: `
    <h1>Sección no encontrada</h1>
    <p class="sub">La dirección no corresponde a ninguna sección. <a routerLink="/panel">Ir al panel</a>.</p>`,
})
export class NoEncontradoComponent {}
