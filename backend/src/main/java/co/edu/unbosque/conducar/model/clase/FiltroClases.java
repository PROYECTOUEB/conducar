package co.edu.unbosque.conducar.model.clase;

import java.time.LocalDate;


public record FiltroClases(
        String estado,
        String tipo,
        Integer idInstructor,
        String placa,
        LocalDate desde,
        LocalDate hasta) {
}
