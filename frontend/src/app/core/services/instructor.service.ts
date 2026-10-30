import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { InstructorResumen } from '../models/instructor.model';

@Injectable({ providedIn: 'root' })
export class InstructorService {
  private readonly http = inject(HttpClient);

  listar(): Observable<InstructorResumen[]> {
    return this.http.get<InstructorResumen[]>('/api/instructores');
  }
}
