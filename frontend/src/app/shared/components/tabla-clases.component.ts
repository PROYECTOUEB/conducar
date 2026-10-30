import { Component, Input, inject } from '@angular/core';
import { Router } from '@angular/router';
import { ClaseResumen } from '../../core/models/clase.model';
import { PIPES } from '../pipes/formato.pipes';
import { EstadoComponent } from './estado.component';

/** Tabla de clases con una columna por dato y el estado (dictada, programada o cancelada) resaltado. */
@Component({
  selector: 'app-tabla-clases',
  imports: [EstadoComponent, PIPES],
  template: `
    @if (clases.length) {
      <div class="tabla-envoltura">
        <table class="tabla-clases">
          <thead>
            <tr>
              <th scope="col" class="num">N.°<code class="col">id_<wbr>clase</code></th>
              <th scope="col">Fecha<code class="col">fecha</code><code class="col">hora_<wbr>inicio, horas</code></th>
              <th scope="col">Módulo<code class="col">id_<wbr>modulo</code><code class="col">modulo.<wbr>tipo</code></th>
              @if (mostrarAlumno) {
                <th scope="col">Matrícula<code class="col">id_<wbr>matricula</code><code class="col">alumno, categoría</code></th>
              } @else {
                <th scope="col">Matrícula<code class="col">id_<wbr>matricula</code><code class="col">codigo_<wbr>categoria</code></th>
              }
              <th scope="col">Instructor<code class="col">id_<wbr>instructor</code></th>
              <th scope="col">Vehículo<code class="col">placa</code></th>
              <th scope="col">Estado<code class="col">estado</code></th>
            </tr>
          </thead>
          <tbody>
            @for (c of clases; track c.idClase) {
              <tr class="fila-enlace" [class.cancelada]="c.estado === 'CANCELADA'" tabindex="0"
                  (click)="abrir(c)" (keydown.enter)="abrir(c)">
                <td class="num">{{ c.idClase }}</td>
                <td class="fecha">{{ c.fecha | fecha }}<span class="linea2">{{ c.horaInicio | hora }} · {{ c.horas }} h</span></td>
                <td><span class="modulo">{{ c.idModulo }} · {{ c.modulo }}</span><span [class]="'tipo ' + c.tipo">{{ c.tipo | tipoClase }}</span></td>
                <td>
                  {{ c.idMatricula }}@if (mostrarAlumno) { · {{ c.alumno }} }
                  <span class="linea2">Categoría {{ c.codigoCategoria }}</span>
                </td>
                <td>{{ c.idInstructor }} · {{ c.instructor }}</td>
                <td>{{ c.placa || 'NULL (aula)' }}</td>
                <td><app-estado [valor]="c.estado" /></td>
              </tr>
            }
          </tbody>
        </table>
      </div>
    } @else {
      <p class="vacio">{{ vacio }}</p>
    }`,
})
export class TablaClasesComponent {
  private readonly router = inject(Router);

  @Input() clases: ClaseResumen[] = [];
  @Input() mostrarAlumno = true;
  @Input() vacio = 'No hay clases para mostrar.';

  abrir(c: ClaseResumen): void {
    this.router.navigate(['/alumnos', c.idAlumno]);
  }
}
