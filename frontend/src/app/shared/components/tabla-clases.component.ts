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
              <th scope="col">Fecha</th>
              <th scope="col">Hora</th>
              <th scope="col" class="num">Horas</th>
              <th scope="col">Tipo</th>
              <th scope="col">Módulo</th>
              @if (mostrarAlumno) { <th scope="col">Alumno</th> }
              <th scope="col">Categoría</th>
              <th scope="col">Instructor</th>
              <th scope="col">Vehículo</th>
              <th scope="col">Estado</th>
            </tr>
          </thead>
          <tbody>
            @for (c of clases; track c.idClase) {
              <tr class="fila-enlace" [class.cancelada]="c.estado === 'CANCELADA'" tabindex="0"
                  (click)="abrir(c)" (keydown.enter)="abrir(c)">
                <td class="fecha">{{ c.fecha | fecha }}</td>
                <td>{{ c.horaInicio | hora }}</td>
                <td class="num">{{ c.horas }}</td>
                <td><span [class]="'tipo ' + c.tipo">{{ c.tipo | tipoClase }}</span></td>
                <td>{{ c.modulo }}</td>
                @if (mostrarAlumno) { <td>{{ c.alumno }}</td> }
                <td>{{ c.codigoCategoria }}</td>
                <td>{{ c.instructor }}</td>
                <td>{{ c.placa || 'Aula' }}</td>
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
