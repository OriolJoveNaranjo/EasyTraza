/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.controller;

import cat.copernic.easytraza.entities.Usuari;
import cat.copernic.easytraza.service.UsuariService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author orjon Controlador web per gestionar usuaris.
 */
@Controller
public class UsuariController {

    private final UsuariService usuariService;

    public UsuariController(UsuariService usuariService) {
        this.usuariService = usuariService;
    }

    @GetMapping("/usuaris")
    public String mostrarUsuaris(Model model) {
        model.addAttribute("usuaris", usuariService.findAll());
        return "usuaris";
    }

    @GetMapping("/usuaris/nou")
    public String mostrarFormulariNouUsuari(Model model) {
        model.addAttribute("usuari", new Usuari());
        model.addAttribute("mode", "create");
        return "nou-usuari";
    }

    @PostMapping("/usuaris/guardar")
    public String guardar(@ModelAttribute Usuari usuari, Model model) {
        try {
            if (usuari.getId() != null) {
                usuariService.update(usuari.getId(), usuari);
            } else {
                usuariService.save(usuari);
            }

            return "redirect:/usuaris";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("usuari", usuari);
            model.addAttribute("mode", usuari.getId() != null ? "edit" : "create");
            return "nou-usuari";
        }
    }

    @GetMapping("/usuaris/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Usuari usuari = usuariService.findById(id).orElse(null);

        if (usuari == null) {
            return "redirect:/usuaris";
        }

        model.addAttribute("usuari", usuari);
        model.addAttribute("mode", "edit");
        return "nou-usuari";
    }

    @GetMapping("/usuaris/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        usuariService.deleteById(id);
        return "redirect:/usuaris";
    }
}
