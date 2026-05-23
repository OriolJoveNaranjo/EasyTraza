package cat.copernic.easytraza.controller;

import cat.copernic.easytraza.entities.Tracabilitat;
import cat.copernic.easytraza.repository.LotProveidorRepository;
import cat.copernic.easytraza.repository.TracabilitatRepository;
import java.util.ArrayList;
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
    /**
     * Executa l'operació TracabilitatController.
     */

    public TracabilitatController(LotProveidorRepository lotProveidorRepository,
            TracabilitatRepository tracabilitatRepository) {
        this.lotProveidorRepository = lotProveidorRepository;
        this.tracabilitatRepository = tracabilitatRepository;
    }
    /**
     * Executa l'operació tracabilitat.
     */

    @GetMapping("/tracabilitat")
    public String tracabilitat(
            @RequestParam(required = false) Long lotId,
            @RequestParam(required = false) String cerca,
            @RequestParam(required = false) String ordre,
            @RequestParam(required = false, defaultValue = "asc") String direccio,
            Model model) {

        List<?> lots = lotProveidorRepository.findAll()
                .stream()
                .filter(lot -> {
                    if (cerca == null || cerca.isBlank()) {
                        return true;
                    }

                    String cercaLower = cerca.toLowerCase().trim();

                    String identificadorLot = lot.getIdentificadorLot() != null
                            ? lot.getIdentificadorLot().toLowerCase()
                            : "";

                    String materia = lot.getMateriaPrimera() != null
                            && lot.getMateriaPrimera().getNom() != null
                            ? lot.getMateriaPrimera().getNom().toLowerCase()
                            : "";

                    String opcioCompleta = identificadorLot + " - " + materia;

                    return identificadorLot.contains(cercaLower)
                            || materia.contains(cercaLower)
                            || opcioCompleta.contains(cercaLower)
                            || cercaLower.contains(identificadorLot)
                            || cercaLower.contains(materia);
                })
                .toList();
        model.addAttribute("lots", lotProveidorRepository.findAll());
        model.addAttribute("lotSeleccionatId", lotId);
        model.addAttribute("cerca", cerca);
        model.addAttribute("ordre", ordre);
        model.addAttribute("direccio", direccio);

        List<Tracabilitat> resultats = new java.util.ArrayList<>();

        if (lotId != null) {
            resultats = tracabilitatRepository.findByLotProveidor_Id(lotId);
        } else if (cerca != null && !cerca.isBlank()) {
            String cercaLower = cerca.toLowerCase();

            resultats = new ArrayList<>(
                    tracabilitatRepository.findAll()
                            .stream()
                            .filter(t -> {
                                boolean coincideixLot = t.getLotProveidor() != null
                                        && t.getLotProveidor().getIdentificadorLot() != null
                                        && (t.getLotProveidor().getIdentificadorLot().toLowerCase().contains(cercaLower)
                                        || cercaLower.contains(t.getLotProveidor().getIdentificadorLot().toLowerCase()));

                                boolean coincideixMateria = t.getLotProveidor() != null
                                        && t.getLotProveidor().getMateriaPrimera() != null
                                        && t.getLotProveidor().getMateriaPrimera().getNom() != null
                                        && (t.getLotProveidor().getMateriaPrimera().getNom().toLowerCase().contains(cercaLower)
                                        || cercaLower.contains(t.getLotProveidor().getMateriaPrimera().getNom().toLowerCase()));

                                return coincideixLot || coincideixMateria;
                            })
                            .toList()
            );
        }

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

        return "tracabilitat";
    }
}
