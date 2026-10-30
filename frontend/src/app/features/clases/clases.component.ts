import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin, switchMap } from 'rxjs';
import { Catalogos } from '../../core/models/catalogo.model';
import { ClaseResumen } from '../../core/models/clase.model';
import { CatalogoService } from '../../core/services/catalogo.service';
import { ClaseService, FiltroClases } from '../../core/services/clase.service';
import { ErrorComponent } from '../../shared/components/error.component';
import { LeyendaComponent } from '../../shared/components/leyenda.component';
import { TiraClaseComponent } from '../../shared/components/tira-clase.component';
import { mensajeError } from '../../shared/formato';
import { PIPES } from '../../shared/pipes/formato.pipes';

@Component({
  selector: 'app-clases',
  imports: [FormsModule, ErrorComponent, LeyendaComponent, TiraClaseComponent, PIPES],
  templateUrl: './clases.component.html',
})
export class ClasesComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly claseService = inject(ClaseService);
  private readonly catalogoService = inject(CatalogoService);

  catalogos?: Catalogos;
  clases?: ClaseResumen[];
  error = '';
  /** Valores del formulario de filtros. */
  filtro: FiltroClases = {};

  ngOnInit(): void {
    // Los filtros viajan en la URL (?estado=...&tipo=...), así se pueden compartir y usar el botón Atrás
    this.route.queryParamMap
      .pipe(
        switchMap((q) => {
          this.filtro = {
            estado: q.get('estado') ?? '', tipo: q.get('tipo') ?? '', instructor: q.get('instructor') ?? '',
            placa: q.get('placa') ?? '', desde: q.get('desde') ?? '', hasta: q.get('hasta') ?? '',
          };
          this.clases = undefined;
          return forkJoin({ catalogos: this.catalogoService.obtener(), clases: this.claseService.listar(this.filtro) });
        }),
      )
      .subscribe({
        next: (r) => {
          this.catalogos = r.catalogos;
          this.clases = r.clases;
        },
        error: (e) => (this.error = mensajeError(e)),
      });
  }

  get horasSeleccion(): number {
    return (this.clases ?? []).filter((c) => c.estado !== 'CANCELADA').reduce((suma, c) => suma + (c.horas ?? 0), 0);
  }

  filtrar(): void {
    const queryParams: Record<string, string> = {};
    for (const [clave, valor] of Object.entries(this.filtro)) {
      if (valor) queryParams[clave] = valor;
    }
    this.router.navigate(['/clases'], { queryParams });
  }

  limpiar(): void {
    this.router.navigate(['/clases']);
  }
}
