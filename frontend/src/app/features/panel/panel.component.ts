import { Component, OnInit, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Alerta, Panel } from '../../core/models/panel.model';
import { PanelService } from '../../core/services/panel.service';
import { Barra, BarrasComponent } from '../../shared/components/barras.component';
import { ErrorComponent } from '../../shared/components/error.component';
import { LeyendaComponent } from '../../shared/components/leyenda.component';
import { TiraClaseComponent } from '../../shared/components/tira-clase.component';
import { fecha, mensajeError } from '../../shared/formato';
import { PIPES } from '../../shared/pipes/formato.pipes';

@Component({
  selector: 'app-panel',
  imports: [RouterLink, BarrasComponent, ErrorComponent, LeyendaComponent, TiraClaseComponent, PIPES],
  templateUrl: './panel.component.html',
})
export class PanelComponent implements OnInit {
  private readonly panelService = inject(PanelService);

  panel?: Panel;
  error = '';
  horasInstructor: Barra[] = [];
  matriculasCategoria: Barra[] = [];
  clasesPorMes: Barra[] = [];

  ngOnInit(): void {
    this.panelService.obtener().subscribe({
      next: (p) => {
        this.panel = p;
        this.horasInstructor = p.horasInstructor.map((h) => ({ etiqueta: h.instructor ?? '', valor: h.horas ?? 0 }));
        this.matriculasCategoria = p.matriculasCategoria.map((m) => ({ etiqueta: `Categoría ${m.categoria}`, valor: m.matriculas ?? 0 }));
        this.clasesPorMes = p.clasesPorMes.map((m) => ({ etiqueta: fecha(m.mes + '-01').slice(2), valor: m.dictadas ?? 0 }));
      },
      error: (e) => (this.error = mensajeError(e)),
    });
  }

  nivel(a: Alerta): string {
    return 'marca-nivel nivel-' + a.nivel;
  }
}
