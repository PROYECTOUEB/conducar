import { Component, OnInit, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { InstructorResumen } from '../../core/models/instructor.model';
import { InstructorService } from '../../core/services/instructor.service';
import { ErrorComponent } from '../../shared/components/error.component';
import { EstadoComponent } from '../../shared/components/estado.component';
import { mensajeError } from '../../shared/formato';
import { PIPES } from '../../shared/pipes/formato.pipes';

@Component({
  selector: 'app-instructores',
  imports: [RouterLink, ErrorComponent, EstadoComponent, PIPES],
  templateUrl: './instructores.component.html',
})
export class InstructoresComponent implements OnInit {
  private readonly instructorService = inject(InstructorService);

  instructores?: InstructorResumen[];
  error = '';

  ngOnInit(): void {
    this.instructorService.listar().subscribe({
      next: (i) => (this.instructores = i),
      error: (e) => (this.error = mensajeError(e)),
    });
  }
}
