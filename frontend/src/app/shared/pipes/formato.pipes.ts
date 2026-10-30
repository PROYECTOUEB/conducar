/** Pipes para mostrar fechas, horas, números, pesos y textos en la interfaz. */
import { Pipe, PipeTransform } from '@angular/core';
import { TIPOS_CLASE, fecha, hora, numero, pesos, texto } from '../formato';

@Pipe({ name: 'fecha' })
export class FechaPipe implements PipeTransform {
  transform(valor: string | null | undefined): string {
    return fecha(valor);
  }
}

@Pipe({ name: 'hora' })
export class HoraPipe implements PipeTransform {
  transform(valor: string | null | undefined): string {
    return hora(valor);
  }
}

@Pipe({ name: 'numero' })
export class NumeroPipe implements PipeTransform {
  transform(valor: number | string | null | undefined, decimales = 1): string {
    return numero(valor, decimales);
  }
}

@Pipe({ name: 'pesos' })
export class PesosPipe implements PipeTransform {
  transform(valor: number | null | undefined): string {
    return pesos(valor);
  }
}

@Pipe({ name: 'texto' })
export class TextoPipe implements PipeTransform {
  transform(valor: string | null | undefined): string {
    return texto(valor);
  }
}

@Pipe({ name: 'tipoClase' })
export class TipoClasePipe implements PipeTransform {
  transform(valor: string | null | undefined): string {
    return valor ? (TIPOS_CLASE[valor] ?? valor) : '—';
  }
}

/** Todos los pipes juntos, para importarlos en los componentes. */
export const PIPES = [FechaPipe, HoraPipe, NumeroPipe, PesosPipe, TextoPipe, TipoClasePipe];
