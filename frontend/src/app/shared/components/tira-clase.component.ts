import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ClaseResumen } from '../../core/models/clase.model';
import { PIPES } from '../pipes/formato.pipes';

/** Tira de clase: una fila por clase, coloreada por tipo (teoría, taller o práctica). */
@Component({
  selector: 'app-tira-clase',
  imports: [RouterLink, PIPES],
  template: `
    <a [class]="'tira ' + clase.tipo + ' ' + clase.estado" [routerLink]="['/alumnos', clase.idAlumno]">
      <div><span class="cod">{{ clase.fecha | fecha }}</span><span class="min">{{ clase.horaInicio | hora }} · {{ clase.horas }} h</span></div>
      <div><span>{{ clase.tipo | tipoClase }}</span><span class="min">{{ clase.modulo }}</span></div>
      <div>
        <span class="ruta">{{ mostrarAlumno ? clase.alumno : 'Matrícula ' + clase.idMatricula }}</span>
        <span class="min">Categoría {{ clase.codigoCategoria }} · {{ clase.estado | texto }}</span>
      </div>
      <div><span>{{ clase.instructor }}</span><span class="min">instructor</span></div>
      <div><span class="cod">{{ clase.placa || '—' }}</span><span class="min">{{ clase.placa ? 'vehículo' : 'aula' }}</span></div>
    </a>`,
})
export class TiraClaseComponent {
  @Input({ required: true }) clase!: ClaseResumen;
  @Input() mostrarAlumno = true;
}
