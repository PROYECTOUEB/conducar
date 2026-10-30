import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { ClaseResumen } from '../models/clase.model';

/** Filtros de la agenda de clases (todos opcionales). */
export interface FiltroClases {
  estado?: string;
  tipo?: string;
  instructor?: string;
  placa?: string;
  desde?: string;
  hasta?: string;
}

@Injectable({ providedIn: 'root' })
export class ClaseService {
  private readonly http = inject(HttpClient);

  listar(filtro: FiltroClases): Observable<ClaseResumen[]> {
    let params = new HttpParams();
    for (const [clave, valor] of Object.entries(filtro)) {
      if (valor) {
        params = params.set(clave, valor);
      }
    }
    return this.http.get<ClaseResumen[]>('/api/clases', { params });
  }
}
