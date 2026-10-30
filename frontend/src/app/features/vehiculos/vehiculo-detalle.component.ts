import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { switchMap } from 'rxjs';
import { VehiculoDetalle } from '../../core/models/vehiculo.model';
import { VehiculoService } from '../../core/services/vehiculo.service';
import { ErrorComponent } from '../../shared/components/error.component';
import { EstadoComponent } from '../../shared/components/estado.component';
import { LeyendaComponent } from '../../shared/components/leyenda.component';
import { TiraClaseComponent } from '../../shared/components/tira-clase.component';
import { mensajeError } from '../../shared/formato';
import { PIPES } from '../../shared/pipes/formato.pipes';

@Component({
  selector: 'app-vehiculo-detalle',
  imports: [RouterLink, ErrorComponent, EstadoComponent, LeyendaComponent, TiraClaseComponent, PIPES],
  templateUrl: './vehiculo-detalle.component.html',
})
export class VehiculoDetalleComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly vehiculoService = inject(VehiculoService);

  detalle?: VehiculoDetalle;
  error = '';

  ngOnInit(): void {
    this.route.paramMap
      .pipe(switchMap((p) => this.vehiculoService.detalle(p.get('placa') ?? '')))
      .subscribe({ next: (d) => (this.detalle = d), error: (e) => (this.error = mensajeError(e)) });
  }
}
