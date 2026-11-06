package co.edu.unbosque.conducar.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Métodos comunes de los repositorios: ejecutar un SELECT con PreparedStatement,
 * asignar los parámetros (?) y leer columnas que pueden ser nulas.
 */
public abstract class RepositorioBase {

    protected final ConexionBD conexionBD;

    protected RepositorioBase(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /** Ejecuta un SELECT y convierte cada fila con el mapeador. Los parámetros reemplazan los ? en orden. */
    protected <T> List<T> consultar(String sql, Mapeador<T> mapeador, Object... parametros) throws SQLException {
        try (Connection conexion = conexionBD.abrir();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            asignar(ps, parametros);
            try (ResultSet rs = ps.executeQuery()) {
                List<T> lista = new ArrayList<>();
                while (rs.next()) {
                    lista.add(mapeador.mapear(rs));
                }
                return lista;
            }
        }
    }

    /** Igual que consultar, pero devuelve solo la primera fila (si existe). */
    protected <T> Optional<T> consultarUno(String sql, Mapeador<T> mapeador, Object... parametros) throws SQLException {
        List<T> lista = consultar(sql, mapeador, parametros);
        return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
    }

    private static void asignar(PreparedStatement ps, Object... parametros) throws SQLException {
        for (int i = 0; i < parametros.length; i++) {
            Object p = parametros[i];
            int posicion = i + 1;
            if (p instanceof LocalDate fecha) {
                ps.setDate(posicion, Date.valueOf(fecha));
            } else if (p instanceof Integer entero) {
                ps.setInt(posicion, entero);
            } else if (p instanceof String texto) {
                ps.setString(posicion, texto);
            } else {
                ps.setObject(posicion, p);
            }
        }
    }

    // ---- Lectura de columnas que pueden venir en NULL ----

    protected static Integer entero(ResultSet rs, String columna) throws SQLException {
        int valor = rs.getInt(columna);
        return rs.wasNull() ? null : valor;
    }

    protected static Double decimal(ResultSet rs, String columna) throws SQLException {
        BigDecimal valor = rs.getBigDecimal(columna);
        return valor == null ? null : valor.doubleValue();
    }

    protected static LocalDate fecha(ResultSet rs, String columna) throws SQLException {
        Date valor = rs.getDate(columna);
        return valor == null ? null : valor.toLocalDate();
    }

    protected static LocalTime hora(ResultSet rs, String columna) throws SQLException {
        Time valor = rs.getTime(columna);
        return valor == null ? null : valor.toLocalTime();
    }
}
