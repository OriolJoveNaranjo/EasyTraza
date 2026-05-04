/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.controller;

import cat.copernic.easytraza.entities.Proveidor;
import cat.copernic.easytraza.service.ProveidorService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author orjon
 *
 * Controlador web per gestionar proveïdors.
 */
@Controller
@RequestMapping("/proveidors")
public class ProveidorController {

    private final ProveidorService proveidorService;

    public ProveidorController(ProveidorService proveidorService) {
        this.proveidorService = proveidorService;
    }

    @GetMapping
    public String mostrarProveidors(
            @RequestParam(required = false) String filtre,
            @RequestParam(required = false) String ordre,
            Model model) {

        List<Proveidor> proveidors = proveidorService.filtrar(filtre, ordre);

        model.addAttribute("proveidors", proveidors);
        model.addAttribute("filtre", filtre);
        model.addAttribute("ordre", ordre);

        return "proveidors";
    }

    @GetMapping("/nou")
    public String mostrarFormulariNouProveidor(Model model) {
        model.addAttribute("proveidor", new Proveidor());
        model.addAttribute("mode", "create");
        return "nou-proveidor";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Proveidor proveidor, Model model) {
        try {
            if (proveidor.getId() != null) {
                proveidorService.update(proveidor.getId(), proveidor);
            } else {
                proveidorService.save(proveidor);
            }

            return "redirect:/proveidors";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("proveidor", proveidor);
            return "nou-proveidor";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Proveidor proveidor = proveidorService.findById(id).orElse(null);

        if (proveidor == null) {
            return "redirect:/proveidors";
        }

        model.addAttribute("proveidor", proveidor);
        model.addAttribute("mode", "edit");
        return "nou-proveidor";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        proveidorService.deleteById(id);
        return "redirect:/proveidors";
    }

    @GetMapping("/veure/{id}")
    public String veure(@PathVariable Long id, Model model) {
        Proveidor proveidor = proveidorService.findById(id).orElse(null);

        if (proveidor == null) {
            return "redirect:/proveidors";
        }

        model.addAttribute("proveidor", proveidor);
        model.addAttribute("mode", "view");
        return "nou-proveidor";
    }
}
