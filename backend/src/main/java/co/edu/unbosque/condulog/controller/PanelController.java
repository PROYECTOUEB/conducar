package co.edu.unbosque.condulog.controller;

import co.edu.unbosque.condulog.model.panel.Panel;
import co.edu.unbosque.condulog.service.PanelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/panel")
public class PanelController {

    private final PanelService panelService;

    public PanelController(PanelService panelService) {
        this.panelService = panelService;
    }

    @GetMapping
    public Panel panel() {
        return panelService.obtenerPanel();
    }
}
