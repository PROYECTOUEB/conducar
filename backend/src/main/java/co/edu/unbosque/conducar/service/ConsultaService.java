package co.edu.unbosque.conducar.service;

import co.edu.unbosque.conducar.exception.RecursoNoEncontradoException;
import co.edu.unbosque.conducar.http.Json;
import co.edu.unbosque.conducar.model.consulta.ConsultaDefinicion;
import co.edu.unbosque.conducar.model.consulta.ConsultaItem;
import co.edu.unbosque.conducar.model.consulta.ConsultaResultado;
import co.edu.unbosque.conducar.model.consulta.ResultadoTabla;
import co.edu.unbosque.conducar.repository.ConsultaRepository;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final List<ConsultaDefinicion> consultas = new ArrayList<>();

    /** Carga las consultas de consultas.json (carpeta resources) al iniciar. */
    public ConsultaService(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
        try (InputStream in = ConsultaService.class.getClassLoader().getResourceAsStream("consultas.json")) {
            if (in == null) {
                throw new IllegalStateException("No se encontró consultas.json en src/main/resources");
            }
            List<?> lista = (List<?>) Json.leer(new String(in.readAllBytes(), StandardCharsets.UTF_8));
            for (Object o : lista) {
                Map<?, ?> c = (Map<?, ?>) o;
                consultas.add(new ConsultaDefinicion((String) c.get("cat"), (String) c.get("q"),
                        (String) c.get("sql"), (String) c.get("exp")));
            }
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo leer consultas.json", e);
        }
    }

    public List<ConsultaItem> catalogo() {
        List<ConsultaItem> items = new ArrayList<>();
        for (int i = 0; i < consultas.size(); i++) {
            ConsultaDefinicion c = consultas.get(i);
            items.add(new ConsultaItem(i + 1, c.cat(), c.q()));
        }
        return items;
    }

    public ConsultaResultado ejecutar(int n) throws SQLException {
        if (n < 1 || n > consultas.size()) {
            throw new RecursoNoEncontradoException("No existe la consulta " + n);
        }
        ConsultaDefinicion c = consultas.get(n - 1);
        String sql = c.sql().trim();
        if (sql.endsWith(";")) {
            sql = sql.substring(0, sql.length() - 1);
        }
        ResultadoTabla r = consultaRepository.ejecutar(sql);
        return new ConsultaResultado(n, c.cat(), c.q(), c.exp(), c.sql(), r.columnas(), r.filas(), r.filas().size());
    }
}
