import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap } from 'rxjs';
import { AlumnoResumen } from '../../core/models/alumno.model';
import { AlumnoService } from '../../core/services/alumno.service';
import { ErrorComponent } from '../../shared/components/error.component';
import { EstadoComponent } from '../../shared/components/estado.component';
import { mensajeError } from '../../shared/formato';
import { PIPES } from '../../shared/pipes/formato.pipes';

@Component({
  selector: 'app-alumnos',
  imports: [FormsModule, ErrorComponent, EstadoComponent, PIPES],
  templateUrl: './alumnos.component.html',
})
export class AlumnosComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly alumnoService = inject(AlumnoService);

  alumnos?: AlumnoResumen[];
  error = '';
  texto = '';
  buscado = '';

  ngOnInit(): void {
    this.route.queryParamMap
      .pipe(
        switchMap((q) => {
          this.texto = this.buscado = q.get('q') ?? '';
          this.alumnos = undefined;
          return this.alumnoService.listar(this.buscado);
        }),
      )
      .subscribe({ next: (a) => (this.alumnos = a), error: (e) => (this.error = mensajeError(e)) });
  }

  buscar(): void {
    const q = this.texto.trim();
    this.router.navigate(['/alumnos'], { queryParams: q ? { q } : {} });
  }

  abrir(a: AlumnoResumen): void {
    this.router.navigate(['/alumnos', a.idAlumno]);
  }
}
