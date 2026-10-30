import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { EstadoConexion } from '../models/estado.model';

@Injectable({ providedIn: 'root' })
export class EstadoService {
  private readonly http = inject(HttpClient);

  consultar(): Observable<EstadoConexion> {
    return this.http.get<EstadoConexion>('/api/estado');
  }
}
