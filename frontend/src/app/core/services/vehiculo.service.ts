import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { VehiculoDetalle, VehiculoResumen } from '../models/vehiculo.model';

@Injectable({ providedIn: 'root' })
export class VehiculoService {
  private readonly http = inject(HttpClient);

  listar(): Observable<VehiculoResumen[]> {
    return this.http.get<VehiculoResumen[]>('/api/vehiculos');
  }

  detalle(placa: string): Observable<VehiculoDetalle> {
    return this.http.get<VehiculoDetalle>(`/api/vehiculos/${encodeURIComponent(placa)}`);
  }
}
