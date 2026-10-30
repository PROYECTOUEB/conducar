import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { MatriculaResumen } from '../models/matricula.model';

@Injectable({ providedIn: 'root' })
export class MatriculaService {
  private readonly http = inject(HttpClient);

  /** estado: 'ACTIVA', 'FINALIZADA', 'CANCELADA' o '' para todas. */
  listar(estado: string): Observable<MatriculaResumen[]> {
    const params = estado ? new HttpParams().set('estado', estado) : new HttpParams();
    return this.http.get<MatriculaResumen[]>('/api/matriculas', { params });
  }
}
