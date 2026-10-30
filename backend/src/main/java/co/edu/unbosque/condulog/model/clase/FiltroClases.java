package co.edu.unbosque.condulog.model.clase;

import java.time.LocalDate;

/** Filtros opcionales de la agenda de clases. */
public record FiltroClases(
        String estado,
        String tipo,
        Integer idInstructor,
        String placa,
        LocalDate desde,
        LocalDate hasta) {
}
