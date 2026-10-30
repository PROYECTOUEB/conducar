package co.edu.unbosque.conducar.service;

import co.edu.unbosque.conducar.exception.RecursoNoEncontradoException;
import co.edu.unbosque.conducar.model.consulta.ConsultaDefinicion;
import co.edu.unbosque.conducar.model.consulta.ConsultaItem;
import co.edu.unbosque.conducar.model.consulta.ConsultaResultado;
import co.edu.unbosque.conducar.model.consulta.ResultadoTabla;
import co.edu.unbosque.conducar.repository.ConsultaRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

/** Consultas SQL de ejemplo (archivo consultas.json) y su ejecución sobre MySQL. */
@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final List<ConsultaDefinicion> consultas;

    public ConsultaService(ConsultaRepository consultaRepository, ObjectMapper mapper) {
        this.consultaRepository = consultaRepository;
        try (InputStream in = new ClassPathResource("consultas.json").getInputStream()) {
            this.consultas = mapper.readValue(in, new TypeReference<List<ConsultaDefinicion>>() { });
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

    public ConsultaResultado ejecutar(int n) {
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
