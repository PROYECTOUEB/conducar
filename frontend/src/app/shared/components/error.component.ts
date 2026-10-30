import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';

/** Mensaje cuando una sección no se pudo cargar. */
@Component({
  selector: 'app-error',
  imports: [RouterLink],
  template: `
    <h1>No fue posible cargar esta sección</h1>
    <div class="error-caja">
      <p>{{ mensaje }}</p>
      <p>Verifique que MySQL y el backend de Spring Boot estén en ejecución. Consulte el
        <a routerLink="/estado">estado de la conexión</a>.</p>
    </div>`,
})
export class ErrorComponent {
  @Input() mensaje = '';
}
