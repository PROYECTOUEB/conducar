import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { of, switchMap } from 'rxjs';
import { ConsultaItem, ConsultaResultado } from '../../core/models/consulta.model';
import { ConsultaService } from '../../core/services/consulta.service';
import { ErrorComponent } from '../../shared/components/error.component';
import { mensajeError } from '../../shared/formato';

interface Grupo {
  categoria: string;
  items: ConsultaItem[];
}

@Component({
  selector: 'app-consultas',
  imports: [RouterLink, ErrorComponent],
  templateUrl: './consultas.component.html',
})
export class ConsultasComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly consultaService = inject(ConsultaService);

  grupos: Grupo[] = [];
  total = 0;
  seleccionada: number | null = null;
  resultado?: ConsultaResultado;
  cargandoResultado = false;
  error = '';

  ngOnInit(): void {
    this.consultaService.catalogo().subscribe({
      next: (lista) => {
        this.total = lista.length;
        this.grupos = [];
        for (const c of lista) {
          const ultimo = this.grupos[this.grupos.length - 1];
          if (ultimo && ultimo.categoria === c.categoria) {
            ultimo.items.push(c);
          } else {
            this.grupos.push({ categoria: c.categoria ?? '', items: [c] });
          }
        }
      },
      error: (e) => (this.error = mensajeError(e)),
    });

    this.route.paramMap
      .pipe(
        switchMap((p) => {
          const n = p.get('n');
          this.seleccionada = n ? Number(n) : null;
          this.resultado = undefined;
          this.cargandoResultado = n !== null;
          return n ? this.consultaService.ejecutar(Number(n)) : of(undefined);
        }),
      )
      .subscribe({
        next: (r) => {
          this.resultado = r;
          this.cargandoResultado = false;
        },
        error: (e) => (this.error = mensajeError(e)),
      });
  }

  valor(v: unknown): string {
    return v === null || v === undefined ? 'NULL' : String(v);
  }
}
