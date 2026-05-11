/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.controller;

import cat.copernic.easytraza.entities.LiniaAlbaraClient;
import cat.copernic.easytraza.entities.LotProveidor;
import cat.copernic.easytraza.entities.Tracabilitat;
import cat.copernic.easytraza.repository.LiniaAlbaraClientRepository;
import cat.copernic.easytraza.repository.LotProveidorRepository;
import cat.copernic.easytraza.repository.TracabilitatRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author orjon
 */
@Controller
public class TracabilitatController {

    private final TracabilitatRepository tracabilitatRepository;
    private final LotProveidorRepository lotProveidorRepository;
    private final LiniaAlbaraClientRepository liniaAlbaraClientRepository;

    public TracabilitatController(
            TracabilitatRepository tracabilitatRepository,
            LotProveidorRepository lotProveidorRepository,
            LiniaAlbaraClientRepository liniaAlbaraClientRepository) {
        this.tracabilitatRepository = tracabilitatRepository;
        this.lotProveidorRepository = lotProveidorRepository;
        this.liniaAlbaraClientRepository = liniaAlbaraClientRepository;
    }

    @GetMapping("/tracabilitat")
    public String llistar(Model model) {
        model.addAttribute("tracabilitats", tracabilitatRepository.findAll());
        return "tracabilitat";
    }

    @GetMapping("/tracabilitat/nova")
    public String nova(Model model) {
        model.addAttribute("lots", lotProveidorRepository.findAll());
        model.addAttribute("linies", liniaAlbaraClientRepository.findAll());
        return "nova-tracabilitat";
    }

    @PostMapping("/tracabilitat/guardar")
    public String guardar(
            @RequestParam Long lotProveidorId,
            @RequestParam Long liniaAlbaraClientId,
            @RequestParam Double quantitatUtilitzada) {

        LotProveidor lot = lotProveidorRepository.findById(lotProveidorId)
                .orElseThrow(() -> new RuntimeException("Lot no trobat"));

        LiniaAlbaraClient linia = liniaAlbaraClientRepository.findById(liniaAlbaraClientId)
                .orElseThrow(() -> new RuntimeException("Línia d'albarà no trobada"));

        Tracabilitat tracabilitat = new Tracabilitat();
        tracabilitat.setLotProveidor(lot);
        tracabilitat.setLiniaAlbaraClient(linia);
        tracabilitat.setProducteFinal(linia.getProducte());
        tracabilitat.setQuantitatUtilitzada(quantitatUtilitzada);
        tracabilitat.setDataRegistre(LocalDateTime.now());

        tracabilitatRepository.save(tracabilitat);

        return "redirect:/tracabilitat";
    }
}
