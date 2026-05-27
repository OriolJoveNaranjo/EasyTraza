package cat.copernic.easytraza.controller;

import cat.copernic.easytraza.entities.LotProveidor;
import cat.copernic.easytraza.enums.EstatLot;
import cat.copernic.easytraza.repository.MateriaPrimeraRepository;
import cat.copernic.easytraza.service.LotProveidorService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import cat.copernic.easytraza.repository.ProveidorRepository;

/**
 *
 * @author orjon
 */
@Controller
@RequestMapping("/lots-proveidor")
public class LotProveidorController {

    private final LotProveidorService lotService;
    private final MateriaPrimeraRepository materiaPrimeraRepository;
    private final ProveidorRepository proveidorRepository;

    /**
     * Executa l'operació LotProveidorController.
     * @param lotService
     * @param materiaPrimeraRepository
     * @param proveidorRepository
     */
    public LotProveidorController(
            LotProveidorService lotService,
            MateriaPrimeraRepository materiaPrimeraRepository,
            ProveidorRepository proveidorRepository
    ) {
        this.lotService = lotService;
        this.materiaPrimeraRepository = materiaPrimeraRepository;
        this.proveidorRepository = proveidorRepository;
    }

    /**
     * Consulta dades i retorna la informació necessària per a la vista o l'API.
     * @param identificador
     * @param estat
     * @param materiaId
     * @param data
     * @param proveidor
     * @param model
     * @return 
     */
    @GetMapping
    public String llistar(
            @RequestParam(required = false) String identificador,
            @RequestParam(required = false) String estat,
            @RequestParam(required = false) Long materiaId,
            @RequestParam(required = false) LocalDate data,
            @RequestParam(required = false) String proveidor,
            Model model) {

        if (estat != null && !estat.isBlank()) {
            EstatLot.valueOf(estat);
        }

        List<LotProveidor> lots = lotService.filtrarLots(
                identificador,
                estat,
                materiaId,
                data
        );
        if (proveidor != null && !proveidor.isBlank()) {
            String proveidorLower = proveidor.toLowerCase();

            lots = lots.stream()
                    .filter(lot -> lot.getProveidor() != null
                    && lot.getProveidor().getNom() != null
                    && lot.getProveidor().getNom().toLowerCase().contains(proveidorLower))
                    .toList();
        }

        model.addAttribute("lots", lots);
        model.addAttribute("materies", materiaPrimeraRepository.findAll());
        model.addAttribute("proveidors", proveidorRepository.findAll()
                .stream()
                .filter(p -> p.isActiu())
                .toList());
        model.addAttribute("identificador", identificador);
        model.addAttribute("estatSeleccionat", estat);
        model.addAttribute("materiaId", materiaId);
        model.addAttribute("data", data);
        model.addAttribute("proveidor", proveidor);

        return "lots-proveidor";
    }

    /**
     * Inicia o obre l'element indicat segons el flux de treball.
     * @param id
     * @param confirmar
     * @param redirect
     * @return 
     */
    @PostMapping("/obrir/{id}")
    public String obrir(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean confirmar,
            RedirectAttributes redirect) {

        try {
            lotService.iniciarLot(id, confirmar);
            redirect.addFlashAttribute("success", "Lot obert correctament");

        } catch (RuntimeException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            redirect.addFlashAttribute("lotPendentInici", id);
        }

        return "redirect:/lots-proveidor";
    }

    /**
     * Finalitza o tanca l'element indicat segons el flux de treball.
     * @param id
     * @param redirect
     * @return 
     */
    @PostMapping("/finalitzar/{id}")
    public String finalitzar(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            lotService.finalitzarLot(id);
            redirect.addFlashAttribute("success", "Lot finalitzat correctament");
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/lots-proveidor";
    }

}
