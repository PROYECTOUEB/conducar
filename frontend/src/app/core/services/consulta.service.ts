import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, shareReplay } from 'rxjs';
import { ConsultaItem, ConsultaResultado } from '../models/consulta.model';

@Injectable({ providedIn: 'root' })
export class ConsultaService {
  private readonly http = inject(HttpClient);
  /** El catálogo de preguntas no cambia: se pide una sola vez. */
  private readonly lista$ = this.http.get<ConsultaItem[]>('/api/consultas').pipe(shareReplay(1));

  catalogo(): Observable<ConsultaItem[]> {
    return this.lista$;
  }

  ejecutar(n: number): Observable<ConsultaResultado> {
    return this.http.get<ConsultaResultado>(`/api/consultas/${n}`);
  }
}
