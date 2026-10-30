import { Routes } from '@angular/router';
import { AlumnoDetalleComponent } from './features/alumnos/alumno-detalle.component';
import { AlumnosComponent } from './features/alumnos/alumnos.component';
import { ClasesComponent } from './features/clases/clases.component';
import { ConsultasComponent } from './features/consultas/consultas.component';
import { CursosComponent } from './features/cursos/cursos.component';
import { EstadoConexionComponent } from './features/estado/estado-conexion.component';
import { InstructoresComponent } from './features/instructores/instructores.component';
import { MatriculasComponent } from './features/matriculas/matriculas.component';
import { NoEncontradoComponent } from './features/no-encontrado.component';
import { PanelComponent } from './features/panel/panel.component';
import { VehiculoDetalleComponent } from './features/vehiculos/vehiculo-detalle.component';
import { VehiculosComponent } from './features/vehiculos/vehiculos.component';

const t = (nombre: string) => `${nombre} – ConduLog`;

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'panel' },
  { path: 'panel', component: PanelComponent, title: t('Panel') },
  { path: 'clases', component: ClasesComponent, title: t('Agenda de clases') },
  { path: 'alumnos', component: AlumnosComponent, title: t('Alumnos') },
  { path: 'alumnos/:id', component: AlumnoDetalleComponent, title: t('Alumno') },
  { path: 'matriculas', component: MatriculasComponent, title: t('Matrículas y pagos') },
  { path: 'instructores', component: InstructoresComponent, title: t('Instructores') },
  { path: 'vehiculos', component: VehiculosComponent, title: t('Vehículos') },
  { path: 'vehiculos/:placa', component: VehiculoDetalleComponent, title: t('Vehículo') },
  { path: 'cursos', component: CursosComponent, title: t('Cursos y categorías') },
  { path: 'consultas', component: ConsultasComponent, title: t('Consultas SQL') },
  { path: 'consultas/:n', component: ConsultasComponent, title: t('Consultas SQL') },
  { path: 'estado', component: EstadoConexionComponent, title: t('Estado de la conexión') },
  { path: '**', component: NoEncontradoComponent, title: t('Sección no encontrada') },
];
