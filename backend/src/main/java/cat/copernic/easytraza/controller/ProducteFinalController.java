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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
            if (producteFinal.getId() != null) {
                producteFinalService.update(producteFinal.getId(), producteFinal);
            } else {
                producteFinalService.save(producteFinal);
            }

            return "redirect:/productes-finals";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("producteFinal", producteFinal);
            return "nou-producte";
        }
    }

    @GetMapping("/productes-finals/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        ProducteFinal producteFinal = producteFinalService.findById(id).orElse(null);

        if (producteFinal == null) {
            return "redirect:/productes-finals";
        }

        model.addAttribute("producteFinal", producteFinal);
        return "nou-producte";
    }

    @GetMapping("/productes-finals/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            String missatge = producteFinalService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", missatge);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/productes-finals";
    }

    @GetMapping("/productes-finals/activar/{id}")
    public String activar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        producteFinalService.activar(id);
        redirectAttributes.addFlashAttribute("success", "Producte final activat correctament.");
        return "redirect:/productes-finals";
    }
}
