package co.edu.unbosque.conducar.model.estado;

import java.util.Map;


public record EstadoConexion(
        String conexion,
        ServidorInfo servidor,
        Integer tablas,
        Map<String, Integer> conteos) {
}
