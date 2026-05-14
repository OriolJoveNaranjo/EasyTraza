/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

/**
 *
 * @author orjon
 */
@Controller
@RequestMapping("/lots-proveidor")
public class LotProveidorController {

    private final LotProveidorService lotService;
    private final MateriaPrimeraRepository materiaPrimeraRepository;

    public LotProveidorController(LotProveidorService lotService, MateriaPrimeraRepository materiaPrimeraRepository) {
        this.lotService = lotService;
        this.materiaPrimeraRepository = materiaPrimeraRepository;
    }

    @GetMapping
    public String llistar(
            @RequestParam(required = false) String identificador,
            @RequestParam(required = false) String estat,
            @RequestParam(required = false) Long materiaId,
            @RequestParam(required = false) LocalDate data,
            Model model) {
        EstatLot estatEnum = null;

        if (estat != null && !estat.isBlank()) {
            estatEnum = EstatLot.valueOf(estat);
        }

        List<LotProveidor> lots = lotService.filtrarLots(
                identificador,
                estat,
                materiaId,
                data
        );

        model.addAttribute("lots", lots);
        model.addAttribute("materies", materiaPrimeraRepository.findAll());

        model.addAttribute("identificador", identificador);
        model.addAttribute("estatSeleccionat", estat);
        model.addAttribute("materiaId", materiaId);
        model.addAttribute("data", data);

        return "lots-proveidor";
    }

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
