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

  ofrecidas: Categoria[] = [];
  otras: Categoria[] = [];
  cargado = false;
  error = '';
  readonly tipos = ['TEORIA', 'TALLER', 'PRACTICA'];

  ngOnInit(): void {
    this.categoriaService.listar().subscribe({
      next: (c) => {
        this.ofrecidas = c.filter((x) => x.valorCurso !== null);
        this.otras = c.filter((x) => x.valorCurso === null);
        this.cargado = true;
      },
      error: (e) => (this.error = mensajeError(e)),
    });
  }

  modulos(c: Categoria, tipo: string): ModuloPlan[] {
    return c.modulos.filter((m) => m.tipo === tipo);
  }

  horas(c: Categoria, tipo: string): number {
    return this.modulos(c, tipo).reduce((suma, m) => suma + (m.horas ?? 0), 0);
  }
}
