/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.controller;

import cat.copernic.easytraza.entities.ControlPh;
import cat.copernic.easytraza.entities.Usuari;
import cat.copernic.easytraza.repository.ControlPhRepository;
import cat.copernic.easytraza.repository.UsuariRepository;
import java.time.LocalDateTime;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author orjon
 */
@Controller
@RequestMapping("/controls")
public class ControlPhController {

    private final ControlPhRepository controlPhRepository;
    private final UsuariRepository usuariRepository;
    /**
     * Executa l'operació ControlPhController.
     */

    public ControlPhController(ControlPhRepository controlPhRepository,
            UsuariRepository usuariRepository) {
        this.controlPhRepository = controlPhRepository;
        this.usuariRepository = usuariRepository;
    }
    /**
     * Consulta dades i retorna la informació necessària per a la vista o l'API.
     */

    @GetMapping
    public String llistar(Model model) {
        model.addAttribute("controls", controlPhRepository.findAllByOrderByDataControlDesc());
        return "controls";
    }
    /**
     * Executa l'operació nou.
     */

    @GetMapping("/nou")
    public String nou(Model model) {
        ControlPh control = new ControlPh();
        control.setValorPh(7.0);

        model.addAttribute("controlPh", control);
        return "control-ph-form";
    }
    /**
     * Valida i desa la informació rebuda.
     */

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute ControlPh controlPh,
            Authentication authentication) {

        Usuari usuari = usuariRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuari no trobat"));

        controlPh.setDataControl(LocalDateTime.now());
        controlPh.setUsuari(usuari);

        controlPhRepository.save(controlPh);

        return "redirect:/controls";
    }
}
