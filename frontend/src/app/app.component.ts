import { Component, OnInit, inject } from '@angular/core';
import { NavigationEnd, Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { filter } from 'rxjs';
import { EstadoService } from './core/services/estado.service';

const CLAVE_MENU = 'conducar.menuPlegado';

/** Estructura general: menú lateral plegable, indicador de conexión y área de contenido. */
@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.component.html',
  host: { '[class.plegado]': 'plegado' },
})
export class AppComponent implements OnInit {
  private readonly estadoService = inject(EstadoService);
  private readonly router = inject(Router);

  /** Íconos de 24x24 dibujados con trazos (sin librerías externas). */
  readonly secciones = [
    { ruta: '/panel', nombre: 'Panel', icono: 'M3 3h8v8H3zM13 3h8v5h-8zM13 10h8v11h-8zM3 13h8v8H3z' },
    { ruta: '/clases', nombre: 'Agenda de clases', icono: 'M4 5h16v16H4zM4 9h16M8 3v4M16 3v4' },
    { ruta: '/alumnos', nombre: 'Alumnos', icono: 'M12 12a4 4 0 1 0 0-8 4 4 0 0 0 0 8zM4 21c0-4 4-6 8-6s8 2 8 6' },
    { ruta: '/matriculas', nombre: 'Matrículas y pagos', icono: 'M6 3h12v18l-3-2-3 2-3-2-3 2zM9 8h6M9 12h6' },
    { ruta: '/instructores', nombre: 'Instructores', icono: 'M4 6h16v13H4zM9 11a2 2 0 1 0 0-4 2 2 0 0 0 0 4zM6 17c0-2 1.5-3 3-3s3 1 3 3M14 9h4M14 13h4' },
    { ruta: '/vehiculos', nombre: 'Vehículos', icono: 'M3 17v-5l2.5-5h13l2.5 5v5zM3 12h18M7 17v2M17 17v2' },
    { ruta: '/cursos', nombre: 'Cursos y categorías', icono: 'M4 5a2 2 0 0 1 2-2h14v16H6a2 2 0 0 0-2 2zM4 21V5' },
    { ruta: '/consultas', nombre: 'Consultas SQL', icono: 'M12 3c4.4 0 8 1.3 8 3s-3.6 3-8 3-8-1.3-8-3 3.6-3 8-3zM4 6v12c0 1.7 3.6 3 8 3s8-1.3 8-3V6M4 12c0 1.7 3.6 3 8 3s8-1.3 8-3' },
  ];

  /** En pantallas pequeñas: menú abierto sobre el contenido. */
  menuAbierto = false;
  /** En pantallas grandes: menú reducido a íconos. */
  plegado = false;
  conexion: 'verificando' | 'ok' | 'error' = 'verificando';
  textoConexion = 'Verificando conexión…';

  ngOnInit(): void {
    try {
      this.plegado = localStorage.getItem(CLAVE_MENU) === '1';
    } catch {
      this.plegado = false;
    }
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

  alternarMenu(): void {
    this.plegado = !this.plegado;
    try {
      localStorage.setItem(CLAVE_MENU, this.plegado ? '1' : '0');
    } catch {
      // el navegador no permite guardar preferencias: solo dura mientras la página esté abierta
    }
  }
}
