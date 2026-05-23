/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.controller;

import cat.copernic.easytraza.entities.LiniaAlbaraClient;
import cat.copernic.easytraza.entities.Tracabilitat;
import cat.copernic.easytraza.enums.EstatLot;
import cat.copernic.easytraza.repository.LiniaAlbaraClientRepository;
import cat.copernic.easytraza.repository.LotProveidorRepository;
import cat.copernic.easytraza.repository.TracabilitatRepository;
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
    private final TracabilitatRepository tracabilitatRepository;
    /**
     * Executa l'operació InformeLotController.
     */

    public InformeLotController(LotProveidorRepository lotProveidorRepository,
            TracabilitatRepository tracabilitatRepository) {
        this.lotProveidorRepository = lotProveidorRepository;
        this.tracabilitatRepository = tracabilitatRepository;
    }
    /**
     * Executa l'operació informeLot.
     */

    @GetMapping("/informes/lot")
    public String informeLot(@RequestParam(required = false) Long lotId, Model model) {

        model.addAttribute("lots",
        lotProveidorRepository.findByEstatNot(EstatLot.EN_ESTOC));
        model.addAttribute("lotSeleccionatId", lotId);

        if (lotId != null) {
            List<Tracabilitat> resultats = tracabilitatRepository.findByLotProveidor_Id(lotId);
            model.addAttribute("resultats", resultats);
        }

        return "informe-lot";
    }
}
