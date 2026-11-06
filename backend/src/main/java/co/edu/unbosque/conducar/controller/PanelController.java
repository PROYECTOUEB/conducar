package co.edu.unbosque.conducar.controller;

import co.edu.unbosque.conducar.http.Peticion;
import co.edu.unbosque.conducar.http.Router;
import co.edu.unbosque.conducar.model.panel.Panel;
import co.edu.unbosque.conducar.service.PanelService;

import java.sql.SQLException;

public class PanelController {

    private final PanelService panelService;

    public PanelController(PanelService panelService) {
        this.panelService = panelService;
    }

    public void registrar(Router router) {
        router.get("/api/panel", this::panel);
    }

    /** GET /api/panel */
    public Panel panel(Peticion p) throws SQLException {
        return panelService.obtenerPanel();
    }
}
