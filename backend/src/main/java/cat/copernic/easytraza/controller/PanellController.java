package cat.copernic.easytraza.controller;

import cat.copernic.easytraza.entities.LotProveidor;
import cat.copernic.easytraza.enums.EstatLot;
import cat.copernic.easytraza.repository.ControlPhRepository;
import cat.copernic.easytraza.repository.LotProveidorRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author orjon Controlador del panel principal después del login.
 */
@Controller
public class PanellController {

    private final LotProveidorRepository lotProveidorRepository;
    private final ControlPhRepository controlPhRepository;
    @Value("${app.superadmin.email}")
    private String superAdminEmail;

    /**
     * Executa l'operació PanellController.
     *
     * @param lotProveidorRepository
     * @param controlPhRepository
     */
    public PanellController(LotProveidorRepository lotProveidorRepository,
            ControlPhRepository controlPhRepository) {
        this.lotProveidorRepository = lotProveidorRepository;
        this.controlPhRepository = controlPhRepository;
    }

    /**
     * Executa l'operació panell.
     *
     * @param model
     * @param authentication
     * @return
     */
    @GetMapping("/panell")
    public String panell(Model model, Authentication authentication) {

        long lotsActius = lotProveidorRepository.countByEstat(EstatLot.OBERT);

        LocalDate avui = LocalDate.now();
        LocalDate enSetDies = avui.plusDays(7);

        long alertesCaducitat = lotProveidorRepository
                .countByDataCaducitatBetween(avui, enSetDies);

        boolean hiHaControlUltimsTresDies = controlPhRepository
                .existsByDataControlAfter(LocalDateTime.now().minusDays(3));

        int controlsPendents = hiHaControlUltimsTresDies ? 0 : 1;
        boolean esSuperAdmin = authentication != null
                && authentication.getName().equalsIgnoreCase(superAdminEmail);

        model.addAttribute("esSuperAdmin", esSuperAdmin);

        model.addAttribute("lotsActius", lotsActius);
        model.addAttribute("alertesCaducitat", alertesCaducitat);
        model.addAttribute("controlsPendents", controlsPendents);

        return "panell";
    }

    /**
     * Consulta dades i retorna la informació necessària per a la vista o l'API.
     *
     * @param model
     * @return
     */
    @GetMapping("/panell/caducitats")
    public String veureCaducitats(Model model) {
        LocalDate avui = LocalDate.now();
        LocalDate limit = avui.plusDays(7);

        List<LotProveidor> lotsCaducitat = lotProveidorRepository
                .findByDataCaducitatBetween(avui, limit);

        model.addAttribute("lotsCaducitat", lotsCaducitat);

        return "caducitats-panell";
    }

}
