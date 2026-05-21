/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.controller.api;

import cat.copernic.easytraza.service.LotProveidorService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author orjon
 */
@RestController
@RequestMapping("/api/mobile/lots-proveidor")
public class MobileLotProveidorApiController {

    private final LotProveidorService lotProveidorService;

    public MobileLotProveidorApiController(LotProveidorService lotProveidorService) {
        this.lotProveidorService = lotProveidorService;
    }

    @PostMapping("/{id}/obrir")
    public ResponseEntity<String> obrirLot(
            @PathVariable Long id,
            @RequestParam Long usuariId,
            @RequestParam(defaultValue = "false") boolean confirmar
    ) {
        try {
            lotProveidorService.iniciarLotMobile(id, confirmar, usuariId);
            return ResponseEntity.ok("Lot obert correctament");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

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

    @PostMapping("/{id}/tancar")
    public ResponseEntity<String> tancarLot(@PathVariable Long id) {
        try {
            lotProveidorService.finalitzarLot(id);
            return ResponseEntity.ok("Lot tancat correctament");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

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
