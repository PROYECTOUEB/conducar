package co.edu.unbosque.conducar.model.estado;


public record ServidorInfo(
        String version,
        String baseDatos,
        String usuario,
        String servidor,
        Integer puerto,
        String fechaServidor,
        Boolean soloLectura) {
}
