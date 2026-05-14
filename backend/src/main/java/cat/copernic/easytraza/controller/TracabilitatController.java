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
 */
@Controller
public class TracabilitatController {

    private final LotProveidorRepository lotProveidorRepository;
    private final TracabilitatRepository tracabilitatRepository;

    public TracabilitatController(LotProveidorRepository lotProveidorRepository,
            TracabilitatRepository tracabilitatRepository) {
        this.lotProveidorRepository = lotProveidorRepository;
        this.tracabilitatRepository = tracabilitatRepository;
    }

    @GetMapping("/tracabilitat")
    public String tracabilitat(@RequestParam(required = false) Long lotId,
            @RequestParam(required = false) String ordre,
            Model model) {

        model.addAttribute("lots", lotProveidorRepository.findByEstatNot(EstatLot.EN_ESTOC));
        model.addAttribute("lotSeleccionatId", lotId);
        model.addAttribute("ordre", ordre);

        if (lotId != null) {
            List<Tracabilitat> resultats = tracabilitatRepository.findByLotProveidor_Id(lotId);

            if (ordre != null && !ordre.isBlank()) {
                switch (ordre) {
                    case "producteAsc" ->
                        resultats.sort((a, b) -> a.getProducteFinal().getNom()
                                .compareToIgnoreCase(b.getProducteFinal().getNom()));

                    case "producteDesc" ->
                        resultats.sort((a, b) -> b.getProducteFinal().getNom()
                                .compareToIgnoreCase(a.getProducteFinal().getNom()));

                    case "quantitatAsc" ->
                        resultats.sort((a, b) -> a.getLiniaAlbaraClient().getQuantitat()
                                .compareTo(b.getLiniaAlbaraClient().getQuantitat()));

                    case "quantitatDesc" ->
                        resultats.sort((a, b) -> b.getLiniaAlbaraClient().getQuantitat()
                                .compareTo(a.getLiniaAlbaraClient().getQuantitat()));

                    case "clientAsc" ->
                        resultats.sort((a, b) -> a.getLiniaAlbaraClient().getAlbaraClient().getClient().getNom()
                                .compareToIgnoreCase(b.getLiniaAlbaraClient().getAlbaraClient().getClient().getNom()));

                    case "clientDesc" ->
                        resultats.sort((a, b) -> b.getLiniaAlbaraClient().getAlbaraClient().getClient().getNom()
                                .compareToIgnoreCase(a.getLiniaAlbaraClient().getAlbaraClient().getClient().getNom()));

                    case "albaraAsc" ->
                        resultats.sort((a, b) -> a.getLiniaAlbaraClient().getAlbaraClient().getId()
                                .compareTo(b.getLiniaAlbaraClient().getAlbaraClient().getId()));

                    case "albaraDesc" ->
                        resultats.sort((a, b) -> b.getLiniaAlbaraClient().getAlbaraClient().getId()
                                .compareTo(a.getLiniaAlbaraClient().getAlbaraClient().getId()));

                    case "dataAsc" ->
                        resultats.sort((a, b) -> a.getLiniaAlbaraClient().getAlbaraClient().getData()
                                .compareTo(b.getLiniaAlbaraClient().getAlbaraClient().getData()));

                    case "dataDesc" ->
                        resultats.sort((a, b) -> b.getLiniaAlbaraClient().getAlbaraClient().getData()
                                .compareTo(a.getLiniaAlbaraClient().getAlbaraClient().getData()));
                }
            }

            model.addAttribute("resultats", resultats);
        }

        return "tracabilitat";
    }
}
