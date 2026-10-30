import { Component, OnInit, inject } from '@angular/core';
import { NavigationEnd, Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { filter } from 'rxjs';
import { EstadoService } from './core/services/estado.service';

/** Estructura general: menú lateral, indicador de conexión y área de contenido. */
@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {
  private readonly estadoService = inject(EstadoService);
  private readonly router = inject(Router);

  readonly secciones = [
    { ruta: '/panel', nombre: 'Panel' },
    { ruta: '/clases', nombre: 'Agenda de clases' },
    { ruta: '/alumnos', nombre: 'Alumnos' },
    { ruta: '/matriculas', nombre: 'Matrículas y pagos' },
    { ruta: '/instructores', nombre: 'Instructores' },
    { ruta: '/vehiculos', nombre: 'Vehículos' },
    { ruta: '/cursos', nombre: 'Cursos y categorías' },
    { ruta: '/consultas', nombre: 'Consultas SQL' },
  ];

  menuAbierto = false;
  conexion: 'verificando' | 'ok' | 'error' = 'verificando';
  textoConexion = 'Verificando conexión…';

  ngOnInit(): void {
    this.router.events.pipe(filter((e) => e instanceof NavigationEnd)).subscribe(() => (this.menuAbierto = false));
    this.estadoService.consultar().subscribe({
      next: (d) => {
        this.conexion = 'ok';
        this.textoConexion = `Conectado a ${d.servidor.baseDatos}`;
      },
      error: () => {
        this.conexion = 'error';
        this.textoConexion = 'Sin conexión a la base de datos';
      },
    });
  }
}
