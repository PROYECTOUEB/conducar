import { Component, OnInit, inject } from '@angular/core';
import { EstadoConexion } from '../../core/models/estado.model';
import { EstadoService } from '../../core/services/estado.service';
import { Barra, BarrasComponent } from '../../shared/components/barras.component';
import { mensajeError } from '../../shared/formato';

@Component({
  selector: 'app-estado-conexion',
  imports: [BarrasComponent],
  templateUrl: './estado-conexion.component.html',
})
export class EstadoConexionComponent implements OnInit {
  private readonly estadoService = inject(EstadoService);

  estado?: EstadoConexion;
  conteos: Barra[] = [];
  error = '';

  ngOnInit(): void {
    this.estadoService.consultar().subscribe({
      next: (d) => {
        this.estado = d;
        this.conteos = Object.entries(d.conteos).map(([tabla, n]) => ({ etiqueta: tabla, valor: n }));
      },
      error: (e) => (this.error = mensajeError(e)),
    });
  }
}
