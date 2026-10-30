import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { switchMap } from 'rxjs';
import { AlumnoDetalle, MatriculaAvance } from '../../core/models/alumno.model';
import { AlumnoService } from '../../core/services/alumno.service';
import { ErrorComponent } from '../../shared/components/error.component';
import { EstadoComponent } from '../../shared/components/estado.component';
import { TablaClasesComponent } from '../../shared/components/tabla-clases.component';
import { mensajeError } from '../../shared/formato';
import { PIPES } from '../../shared/pipes/formato.pipes';

/** Avance de un tipo de clase (teoría, taller o práctica) dentro de una matrícula. */
interface Avance {
  tipo: string;
  dictadas: number;
  plan: number;
}

@Component({
  selector: 'app-alumno-detalle',
  imports: [RouterLink, ErrorComponent, EstadoComponent, TablaClasesComponent, PIPES],
  templateUrl: './alumno-detalle.component.html',
})
export class AlumnoDetalleComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly alumnoService = inject(AlumnoService);

  detalle?: AlumnoDetalle;
  error = '';

  ngOnInit(): void {
    this.route.paramMap
      .pipe(switchMap((p) => this.alumnoService.detalle(Number(p.get('id')))))
      .subscribe({ next: (d) => (this.detalle = d), error: (e) => (this.error = mensajeError(e)) });
  }

  /** Último resultado del examen médico del CRC. */
  get estadoCrc(): string {
    const certificados = this.detalle?.certificados ?? [];
    return certificados.length ? (certificados[0].resultado ?? 'SIN REGISTRO') : 'SIN REGISTRO';
  }

  avances(m: MatriculaAvance): Avance[] {
    return [
      { tipo: 'Teoría', dictadas: m.horasTeoriaDictadas ?? 0, plan: m.horasTeoriaPlan ?? 0 },
      { tipo: 'Taller', dictadas: m.horasTallerDictadas ?? 0, plan: m.horasTallerPlan ?? 0 },
      { tipo: 'Práctica', dictadas: m.horasPracticaDictadas ?? 0, plan: m.horasPracticaPlan ?? 0 },
    ];
  }

  porcentaje(a: Avance): number {
    return a.plan ? Math.min(100, (100 * a.dictadas) / a.plan) : 0;
  }

  saldo(m: MatriculaAvance): number {
    return (m.valorPactado ?? 0) - (m.totalPagado ?? 0);
  }
}
