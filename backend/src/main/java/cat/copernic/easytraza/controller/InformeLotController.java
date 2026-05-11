/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.controller;


import cat.copernic.easytraza.entities.LiniaAlbaraClient;
import cat.copernic.easytraza.repository.LiniaAlbaraClientRepository;
import cat.copernic.easytraza.repository.LotProveidorRepository;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 
 * @author orjon
 * 
 * RF20 - Informe per lot.
 */
@Controller
public class InformeLotController {

    private final LotProveidorRepository lotProveidorRepository;
    private final LiniaAlbaraClientRepository liniaAlbaraClientRepository;

    public InformeLotController(LotProveidorRepository lotProveidorRepository,
                                LiniaAlbaraClientRepository liniaAlbaraClientRepository) {
        this.lotProveidorRepository = lotProveidorRepository;
        this.liniaAlbaraClientRepository = liniaAlbaraClientRepository;
    }

    @GetMapping("/informes/lot")
    public String informeLot(@RequestParam(required = false) Long lotId, Model model) {

        model.addAttribute("lots", lotProveidorRepository.findAll());
        model.addAttribute("lotSeleccionatId", lotId);

        if (lotId != null) {
            List<LiniaAlbaraClient> resultats = liniaAlbaraClientRepository.findByLotProveidorId(lotId);
            model.addAttribute("resultats", resultats);
        }

        return "informe-lot";
    }
}
