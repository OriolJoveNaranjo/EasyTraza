/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.controller;

import cat.copernic.easytraza.service.LotProveidorService;
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

    public LotProveidorController(LotProveidorService lotService) {
        this.lotService = lotService;
    }

    @GetMapping
    public String mostrarLots(
            @RequestParam(required = false) String estat,
            Model model) {

        model.addAttribute("lots", lotService.findByEstat(estat));
        model.addAttribute("estatSeleccionat", estat);
        model.addAttribute("activePage", "lots-proveidor");

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
