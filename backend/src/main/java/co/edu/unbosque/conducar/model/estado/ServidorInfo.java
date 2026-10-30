package co.edu.unbosque.conducar.model.estado;

/** Datos del servidor MySQL al que está conectada la aplicación. */
public record ServidorInfo(
        String version,
        String baseDatos,
        String usuario,
        String servidor,
        Integer puerto,
        String fechaServidor,
        Boolean soloLectura) {
}
