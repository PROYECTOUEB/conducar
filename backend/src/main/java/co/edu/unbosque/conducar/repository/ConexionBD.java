package co.edu.unbosque.conducar.repository;

import co.edu.unbosque.conducar.config.Configuracion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Abre las conexiones con MySQL usando JDBC (DriverManager).
 * Cada consulta abre una conexión y la cierra al terminar (try-with-resources).
 */
public class ConexionBD {

    private final String url;
    private final String usuario;
    private final String contrasena;

    public ConexionBD(Configuracion config) {
        this.url = "jdbc:mysql://" + config.valor("DB_HOST") + ":" + config.valor("DB_PORT") + "/" + config.valor("DB_NAME")
                + "?serverTimezone=America/Bogota&characterEncoding=UTF-8";
        this.usuario = config.valor("DB_USER");
        this.contrasena = config.valor("DB_PASSWORD");
    }

    /** Abre una conexión nueva en modo solo lectura (la aplicación solo consulta). */
    public Connection abrir() throws SQLException {
        Connection conexion = DriverManager.getConnection(url, usuario, contrasena);
        conexion.setReadOnly(true);
        return conexion;
    }

    public String getUrl() {
        return url;
    }

    public String getUsuario() {
        return usuario;
    }
}
