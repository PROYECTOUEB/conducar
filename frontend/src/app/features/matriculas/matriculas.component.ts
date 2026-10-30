import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap } from 'rxjs';
import { MatriculaResumen } from '../../core/models/matricula.model';
import { MatriculaService } from '../../core/services/matricula.service';
import { ErrorComponent } from '../../shared/components/error.component';
import { EstadoComponent } from '../../shared/components/estado.component';
import { mensajeError } from '../../shared/formato';
import { PIPES } from '../../shared/pipes/formato.pipes';

@Component({
  selector: 'app-matriculas',
  imports: [FormsModule, ErrorComponent, EstadoComponent, PIPES],
  templateUrl: './matriculas.component.html',
})
export class MatriculasComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly matriculaService = inject(MatriculaService);

  matriculas?: MatriculaResumen[];
  error = '';
  estado = '';

  ngOnInit(): void {
    this.route.queryParamMap
      .pipe(
        switchMap((q) => {
          this.estado = q.get('estado') ?? '';
          this.matriculas = undefined;
          return this.matriculaService.listar(this.estado);
        }),
      )
      .subscribe({ next: (m) => (this.matriculas = m), error: (e) => (this.error = mensajeError(e)) });
  }

  get totalPagado(): number {
    return (this.matriculas ?? []).reduce((suma, m) => suma + (m.totalPagado ?? 0), 0);
  }

  get totalSaldo(): number {
    return (this.matriculas ?? []).filter((m) => m.estado !== 'CANCELADA').reduce((suma, m) => suma + (m.saldo ?? 0), 0);
  }

  avance(m: MatriculaResumen): number {
    return m.horasPlan ? Math.min(100, (100 * (m.horasDictadas ?? 0)) / m.horasPlan) : 0;
  }

  cambiarEstado(estado: string): void {
    this.router.navigate(['/matriculas'], { queryParams: estado ? { estado } : {} });
  }

  abrir(m: MatriculaResumen): void {
    this.router.navigate(['/alumnos', m.idAlumno]);
  }
}
