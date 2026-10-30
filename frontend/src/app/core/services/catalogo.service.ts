import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Catalogos } from '../models/catalogo.model';

@Injectable({ providedIn: 'root' })
export class CatalogoService {
  private readonly http = inject(HttpClient);

  obtener(): Observable<Catalogos> {
    return this.http.get<Catalogos>('/api/catalogos');
  }
}
