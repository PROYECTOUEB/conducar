package co.edu.unbosque.conducar.repository;

import co.edu.unbosque.conducar.model.consulta.ResultadoTabla;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** Ejecuta las consultas del documento de álgebra relacional y devuelve columnas y filas. */
public class ConsultaRepository extends RepositorioBase {

    public ConsultaRepository(ConexionBD conexionBD) {
        super(conexionBD);
    }

    /** El SQL viene de consultas.json (no del usuario), por eso se ejecuta con un Statement. */
    public ResultadoTabla ejecutar(String sql) throws SQLException {
        try (Connection conexion = conexionBD.abrir();
             Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            ResultSetMetaData meta = rs.getMetaData();
            List<String> columnas = new ArrayList<>();
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                columnas.add(meta.getColumnLabel(i));
            }
            List<List<Object>> filas = new ArrayList<>();
            while (rs.next()) {
                List<Object> fila = new ArrayList<>();
                for (int i = 1; i <= columnas.size(); i++) {
                    Object valor = rs.getObject(i);
                    if (valor instanceof java.util.Date || valor instanceof java.time.temporal.Temporal) {
                        valor = valor.toString();          // fechas y horas como texto
                    } else if (valor instanceof BigDecimal d) {
                        valor = d.doubleValue();           // DECIMAL como número
                    }
                    fila.add(valor);
                }
                filas.add(fila);
            }
            return new ResultadoTabla(columnas, filas);
        }
    }
}
