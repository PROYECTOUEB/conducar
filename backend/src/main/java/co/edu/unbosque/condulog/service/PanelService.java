package co.edu.unbosque.condulog.service;

import co.edu.unbosque.condulog.model.panel.Panel;
import co.edu.unbosque.condulog.repository.ClaseRepository;
import co.edu.unbosque.condulog.repository.PanelRepository;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

/** Arma el panel a partir de varias consultas. */
@Service
public class PanelService {

    private final PanelRepository panelRepository;
    private final ClaseRepository claseRepository;

    public PanelService(PanelRepository panelRepository, ClaseRepository claseRepository) {
        this.panelRepository = panelRepository;
        this.claseRepository = claseRepository;
    }

    public Panel obtenerPanel() {
        LocalDate hoy = LocalDate.now();
        return new Panel(
                panelRepository.indicadores(),
                panelRepository.alertas(hoy, hoy.plusDays(30), hoy.plusDays(60)),
                panelRepository.horasPorInstructor(),
                panelRepository.matriculasPorCategoria(),
                panelRepository.clasesPorMes(),
                claseRepository.proximas(8));
    }
}
