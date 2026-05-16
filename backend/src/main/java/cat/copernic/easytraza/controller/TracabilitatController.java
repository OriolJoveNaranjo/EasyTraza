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
import java.util.Comparator;
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
    public String tracabilitat(
            @RequestParam(required = false) Long lotId,
            @RequestParam(required = false) String ordre,
            @RequestParam(required = false, defaultValue = "asc") String direccio,
            Model model) {

        model.addAttribute("lots", lotProveidorRepository.findAll());
        model.addAttribute("lotSeleccionatId", lotId);
        model.addAttribute("ordre", ordre);
        model.addAttribute("direccio", direccio);

        if (lotId != null) {
            List<Tracabilitat> resultats = tracabilitatRepository.findByLotProveidor_Id(lotId);

            Comparator<Tracabilitat> comparator = switch (ordre == null ? "" : ordre) {
                case "producte" ->
                    Comparator.comparing(t -> t.getProducteFinal().getNom(), String.CASE_INSENSITIVE_ORDER);
                case "quantitat" ->
                    Comparator.comparing(t -> t.getLiniaAlbaraClient().getQuantitat());
                case "client" ->
                    Comparator.comparing(t -> t.getLiniaAlbaraClient().getAlbaraClient().getClient().getNom(), String.CASE_INSENSITIVE_ORDER);
                case "albara" ->
                    Comparator.comparing(t -> t.getLiniaAlbaraClient().getAlbaraClient().getId());
                case "data" ->
                    Comparator.comparing(t -> t.getLiniaAlbaraClient().getAlbaraClient().getData());
                case "lot" ->
                    Comparator.comparing(t -> t.getLotProveidor().getIdentificadorLot(), String.CASE_INSENSITIVE_ORDER);
                case "materia" ->
                    Comparator.comparing(t -> t.getLotProveidor().getMateriaPrimera().getNom(), String.CASE_INSENSITIVE_ORDER);
                case "estat" ->
                    Comparator.comparing(t -> t.getLiniaAlbaraClient().getAlbaraClient().getEstat().name());
                default ->
                    Comparator.comparing(t -> t.getId());
            };

            if ("desc".equals(direccio)) {
                comparator = comparator.reversed();
            }

            resultats.sort(comparator);

            model.addAttribute("resultats", resultats);
        }

        return "tracabilitat";
    }
}
