package cat.copernic.easytraza.controller.api;

import cat.copernic.easytraza.service.LotProveidorService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * API mòbil per obrir, tancar i consultar lots de proveïdor.
 *
 * <p>Permet que l'aplicació Android treballi amb lots en estoc i lots oberts
 * sense dependre de la sessió web tradicional.</p>
 *
 * @author orjon
 */
@RestController
@RequestMapping("/api/mobile/lots-proveidor")
public class MobileLotProveidorApiController {

    private static final Logger logger = LoggerFactory.getLogger(MobileLotProveidorApiController.class);

    private final LotProveidorService lotProveidorService;

    /**
     * Crea el controlador mòbil de lots.
     *
     * @param lotProveidorService servei de lots de proveïdor
     */
    public MobileLotProveidorApiController(LotProveidorService lotProveidorService) {
        this.lotProveidorService = lotProveidorService;
    }

    /**
     * Obre un lot des del mòbil. Si hi ha un lot obert de la mateixa matèria,
     * pot requerir confirmació per tancar l'anterior.
     *
     * @param id identificador del lot
     * @param usuariId usuari que obre el lot
     * @param confirmar indica si es confirma el tancament del lot anterior
     * @return resposta HTTP amb el resultat de l'operació
     */
    @PostMapping("/{id}/obrir")
    public ResponseEntity<String> obrirLot(
            @PathVariable Long id,
            @RequestParam Long usuariId,
            @RequestParam(defaultValue = "false") boolean confirmar
    ) {
        try {
            lotProveidorService.iniciarLotMobile(id, confirmar, usuariId);
            logger.info("Lot {} obert des del mòbil per l'usuari {}", id, usuariId);
            return ResponseEntity.ok("Lot obert correctament");
        } catch (RuntimeException e) {
            logger.warn("No s'ha pogut obrir el lot {} des del mòbil: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Retorna els lots en estat EN_ESTOC disponibles per obrir.
     *
     * @return llista de lots en estoc
     */
    @GetMapping("/en-estoc")
    public List<LotOberturaDto> getLotsEnEstoc() {
        return lotProveidorService.findByEstat("EN_ESTOC")
                .stream()
                .map(lot -> new LotOberturaDto(
                lot.getId(),
                lot.getIdentificadorLot(),
                lot.getMateriaPrimera().getNom(),
                lot.getProveidor().getNom(),
                lot.getQuantitat(),
                lot.getUnitat(),
                lot.getEstat().name()
        ))
                .toList();
    }

    /**
     * Dades resumides d'un lot per a les pantalles mòbils d'obertura i
     * tancament.
     *
     * @param id identificador intern del lot
     * @param identificadorLot codi visible del lot
     * @param materiaPrimera nom de la matèria primera
     * @param proveidor nom del proveïdor
     * @param quantitat quantitat del lot
     * @param unitat unitat de mesura
     * @param estat estat actual del lot
     */
    public record LotOberturaDto(
            Long id,
            String identificadorLot,
            String materiaPrimera,
            String proveidor,
            Double quantitat,
            String unitat,
            String estat
            ) {

    }

    /**
     * Tanca un lot obert des del mòbil.
     *
     * @param id identificador del lot
     * @return resposta HTTP amb el resultat de l'operació
     */
    @PostMapping("/{id}/tancar")
    public ResponseEntity<String> tancarLot(@PathVariable Long id) {
        try {
            lotProveidorService.finalitzarLot(id);
            logger.info("Lot {} tancat des del mòbil", id);
            return ResponseEntity.ok("Lot tancat correctament");
        } catch (RuntimeException e) {
            logger.warn("No s'ha pogut tancar el lot {} des del mòbil: {}", id, e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Retorna els lots actualment oberts.
     *
     * @return llista de lots oberts
     */
    @GetMapping("/oberts")
    public List<LotOberturaDto> getLotsOberts() {
        return lotProveidorService.findByEstat("OBERT")
                .stream()
                .map(lot -> new LotOberturaDto(
                lot.getId(),
                lot.getIdentificadorLot(),
                lot.getMateriaPrimera().getNom(),
                lot.getProveidor().getNom(),
                lot.getQuantitat(),
                lot.getUnitat(),
                lot.getEstat().name()
        ))
                .toList();
    }
}
