package co.edu.unbosque.conducar.controller;

import co.edu.unbosque.conducar.http.Peticion;
import co.edu.unbosque.conducar.http.Router;
import co.edu.unbosque.conducar.model.consulta.ConsultaItem;
import co.edu.unbosque.conducar.model.consulta.ConsultaResultado;
import co.edu.unbosque.conducar.service.ConsultaService;

import java.sql.SQLException;
import java.util.List;

public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    public void registrar(Router router) {
        router.get("/api/consultas", this::catalogo)
              .get("/api/consultas/{n}", this::ejecutar);
    }

    /** GET /api/consultas */
    public List<ConsultaItem> catalogo(Peticion p) {
        return consultaService.catalogo();
    }

    /** GET /api/consultas/{n}: ejecuta la consulta n en MySQL */
    public ConsultaResultado ejecutar(Peticion p) throws SQLException {
        return consultaService.ejecutar(p.rutaEntero("n"));
    }
}
