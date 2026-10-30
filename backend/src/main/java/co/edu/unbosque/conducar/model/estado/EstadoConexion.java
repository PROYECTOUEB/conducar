package co.edu.unbosque.conducar.model.estado;

import java.util.Map;

/** Estado de la conexión y conteo de registros. */
public record EstadoConexion(
        String conexion,
        ServidorInfo servidor,
        Integer tablas,
        Map<String, Integer> conteos) {
}
