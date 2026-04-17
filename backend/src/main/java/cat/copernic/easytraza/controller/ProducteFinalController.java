/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.controller;

import cat.copernic.easytraza.entities.ProducteFinal;
import cat.copernic.easytraza.service.ProducteFinalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author orjon C
 */
@Controller
public class ProducteFinalController {

    private final ProducteFinalService producteFinalService;

    public ProducteFinalController(ProducteFinalService producteFinalService) {
        this.producteFinalService = producteFinalService;
    }

    @GetMapping("/productes-finals")
    public String mostrarProductesFinals(Model model) {
        model.addAttribute("productesFinals", producteFinalService.findAll());
        return "productes-finals";
    }

    @GetMapping("/productes-finals/nou")
    public String mostrarFormulariNouProducteFinal(Model model) {
        model.addAttribute("producteFinal", new ProducteFinal());
        return "nou-producte";
    }

    @PostMapping("/productes-finals/guardar")
    public String guardar(@ModelAttribute ProducteFinal producteFinal, Model model) {
        try {
            producteFinalService.save(producteFinal);
            return "redirect:/productes-finals";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("producteFinal", producteFinal);
            return "nou-producte";
        }
    }
}
