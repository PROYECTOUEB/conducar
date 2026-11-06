package co.edu.unbosque.conducar.http;

/** Método de un controlador que atiende una ruta y devuelve el objeto que se enviará como JSON. */
@FunctionalInterface
public interface Accion {
    Object ejecutar(Peticion peticion) throws Exception;
}
