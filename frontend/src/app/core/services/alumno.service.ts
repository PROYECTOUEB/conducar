import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { AlumnoDetalle, AlumnoResumen } from '../models/alumno.model';

@Injectable({ providedIn: 'root' })
export class AlumnoService {
  private readonly http = inject(HttpClient);

  listar(texto: string): Observable<AlumnoResumen[]> {
    const params = texto ? new HttpParams().set('q', texto) : new HttpParams();
    return this.http.get<AlumnoResumen[]>('/api/alumnos', { params });
  }

  detalle(id: number): Observable<AlumnoDetalle> {
    return this.http.get<AlumnoDetalle>(`/api/alumnos/${id}`);
  }
}
