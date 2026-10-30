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
              <th scope="col" class="num">N.°<code class="col">id_clase</code></th>
              <th scope="col">Fecha<code class="col">fecha</code></th>
              <th scope="col">Hora<code class="col">hora_inicio</code></th>
              <th scope="col" class="num">Horas<code class="col">horas</code></th>
              <th scope="col">Tipo<code class="col">modulo.tipo</code></th>
              <th scope="col">Módulo<code class="col">id_modulo</code></th>
              <th scope="col" class="num">Matrícula<code class="col">id_matricula</code></th>
              @if (mostrarAlumno) { <th scope="col">Alumno<code class="col">alumno</code></th> }
              <th scope="col">Categoría<code class="col">codigo_categoria</code></th>
              <th scope="col">Instructor<code class="col">id_instructor</code></th>
              <th scope="col">Vehículo<code class="col">placa</code></th>
              <th scope="col">Estado<code class="col">estado</code></th>
            </tr>
          </thead>
          <tbody>
            @for (c of clases; track c.idClase) {
              <tr class="fila-enlace" [class.cancelada]="c.estado === 'CANCELADA'" tabindex="0"
                  (click)="abrir(c)" (keydown.enter)="abrir(c)">
                <td class="num">{{ c.idClase }}</td>
                <td class="fecha">{{ c.fecha | fecha }}</td>
                <td>{{ c.horaInicio | hora }}</td>
                <td class="num">{{ c.horas }}</td>
                <td><span [class]="'tipo ' + c.tipo">{{ c.tipo | tipoClase }}</span></td>
                <td>{{ c.idModulo }} · {{ c.modulo }}</td>
                <td class="num">{{ c.idMatricula }}</td>
                @if (mostrarAlumno) { <td>{{ c.alumno }}</td> }
                <td>{{ c.codigoCategoria }}</td>
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
