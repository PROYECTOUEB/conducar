import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { VehiculoResumen } from '../../core/models/vehiculo.model';
import { VehiculoService } from '../../core/services/vehiculo.service';
import { ErrorComponent } from '../../shared/components/error.component';
import { EstadoComponent } from '../../shared/components/estado.component';
import { mensajeError } from '../../shared/formato';
import { PIPES } from '../../shared/pipes/formato.pipes';

@Component({
  selector: 'app-vehiculos',
  imports: [ErrorComponent, EstadoComponent, PIPES],
  templateUrl: './vehiculos.component.html',
})
export class VehiculosComponent implements OnInit {
  private readonly vehiculoService = inject(VehiculoService);
  private readonly router = inject(Router);

  vehiculos?: VehiculoResumen[];
  error = '';

  ngOnInit(): void {
    this.vehiculoService.listar().subscribe({
      next: (v) => (this.vehiculos = v),
      error: (e) => (this.error = mensajeError(e)),
    });
  }

  abrir(v: VehiculoResumen): void {
    this.router.navigate(['/vehiculos', v.placa]);
  }
}
