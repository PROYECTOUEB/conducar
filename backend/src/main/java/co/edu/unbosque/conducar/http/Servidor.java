package co.edu.unbosque.conducar.http;

import co.edu.unbosque.conducar.exception.ManejadorErrores;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;

/**
 * Servidor HTTP del propio JDK (com.sun.net.httpserver). Recibe cada petición, busca la ruta en el Router,
 * ejecuta el controlador y responde en JSON. Los errores se convierten en { "mensaje": ... }.
 */
public class Servidor {

    private static final Set<String> ORIGENES = Set.of("http://localhost:4200", "http://127.0.0.1:4200");

    private final HttpServer server;
    private final Router router;

    public Servidor(int puerto, Router router) throws IOException {
        this.router = router;
        this.server = HttpServer.create(new InetSocketAddress(puerto), 0);
        this.server.createContext("/", this::atender);
        this.server.setExecutor(Executors.newFixedThreadPool(8));
    }

    public void iniciar() {
        server.start();
    }

    public void detener() {
        server.stop(0);
    }

    private void atender(HttpExchange ex) throws IOException {
        long inicio = System.currentTimeMillis();
        int estado;
        String cuerpo;
        String metodo = ex.getRequestMethod();
        String camino = URLDecoder.decode(ex.getRequestURI().getRawPath(), StandardCharsets.UTF_8);

        // Permite que Angular (puerto 4200) llame a la API directamente
        String origen = ex.getRequestHeaders().getFirst("Origin");
        if (origen != null && ORIGENES.contains(origen)) {
            ex.getResponseHeaders().set("Access-Control-Allow-Origin", origen);
            ex.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, OPTIONS");
            ex.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        }

        if ("OPTIONS".equals(metodo)) {
            ex.sendResponseHeaders(204, -1);
            ex.close();
            return;
        }

        Router.Coincidencia c = router.buscar(camino);
        if (!"GET".equals(metodo)) {
            estado = 405;
            cuerpo = Json.escribir(Map.of("mensaje", "Solo se permiten consultas (GET)"));
        } else if (c == null) {
            estado = 404;
            cuerpo = Json.escribir(Map.of("mensaje", "No existe la ruta " + camino));
        } else {
            try {
                Object resultado = c.accion().ejecutar(new Peticion(c.parametros(), ex.getRequestURI().getRawQuery()));
                estado = 200;
                cuerpo = Json.escribir(resultado);
            } catch (Exception e) {
                ManejadorErrores.Respuesta r = ManejadorErrores.manejar(e);
                estado = r.estado();
                cuerpo = Json.escribir(Map.of("mensaje", r.mensaje()));
            }
        }

        byte[] bytes = cuerpo.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        ex.sendResponseHeaders(estado, bytes.length);
        try (OutputStream out = ex.getResponseBody()) {
            out.write(bytes);
        }
        System.out.printf("%s %s -> %d (%d ms)%n", metodo, camino, estado, System.currentTimeMillis() - inicio);
    }
}
