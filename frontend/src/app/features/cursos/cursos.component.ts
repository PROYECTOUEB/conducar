import { Component, OnInit, inject } from '@angular/core';
import { Categoria, ModuloPlan } from '../../core/models/categoria.model';
import { CategoriaService } from '../../core/services/categoria.service';
import { ErrorComponent } from '../../shared/components/error.component';
import { mensajeError } from '../../shared/formato';
import { PIPES } from '../../shared/pipes/formato.pipes';

@Component({
  selector: 'app-cursos',
  imports: [ErrorComponent, PIPES],
  templateUrl: './cursos.component.html',
})
export class CursosComponent implements OnInit {
  private readonly categoriaService = inject(CategoriaService);

  todas: Categoria[] = [];
  ofrecidas: Categoria[] = [];
  cargado = false;
  error = '';
  ngOnInit(): void {
    this.categoriaService.listar().subscribe({
      next: (c) => {
        this.todas = c;
        this.ofrecidas = c.filter((x) => x.valorCurso !== null || x.modulos.length > 0);
        this.cargado = true;
      },
      error: (e) => (this.error = mensajeError(e)),
    });
  }

  /** Horas del plan de un tipo de clase (teoría, taller o práctica). */
  horas(c: Categoria, tipo: string): number {
    return c.modulos.filter((m: ModuloPlan) => m.tipo === tipo).reduce((suma, m) => suma + (m.horas ?? 0), 0);
  }
}
