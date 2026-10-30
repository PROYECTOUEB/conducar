import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Panel } from '../models/panel.model';

@Injectable({ providedIn: 'root' })
export class PanelService {
  private readonly http = inject(HttpClient);

  obtener(): Observable<Panel> {
    return this.http.get<Panel>('/api/panel');
  }
}
