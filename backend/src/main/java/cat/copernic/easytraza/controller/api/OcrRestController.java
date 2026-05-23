/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.controller.api;

import cat.copernic.easytraza.dto.OcrResultDto;
import cat.copernic.easytraza.entities.AlbaraProveidor;
import cat.copernic.easytraza.entities.LiniaAlbaraProveidor;
import cat.copernic.easytraza.entities.LotProveidor;
import cat.copernic.easytraza.service.MateriaPrimeraService;
import cat.copernic.easytraza.service.OcrService;
import cat.copernic.easytraza.service.ProveidorService;
import java.time.LocalDate;
import java.util.ArrayList;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author orjon
 */
@RestController
@RequestMapping("/api/ocr")
public class OcrRestController {

    private final OcrService ocrService;
    private final ProveidorService proveidorService;
    private final MateriaPrimeraService materiaPrimeraService;
    /**
     * Executa l'operació OcrRestController.
     */

    public OcrRestController(OcrService ocrService, ProveidorService proveidorService,MateriaPrimeraService materiaPrimeraService ) {
        this.ocrService = ocrService;
        this.proveidorService = proveidorService;
        this.materiaPrimeraService = materiaPrimeraService;
    }
    /**
     * Consulta dades i retorna la informació necessària per a la vista o l'API.
     */

    @GetMapping("/albarans-proveidor/ocr")
    public String mostrarFormulariOcr(Model model) {
        AlbaraProveidor albara = new AlbaraProveidor();
        albara.setDataRecepcio(LocalDate.now());

        if (albara.getLinies() == null) {
            albara.setLinies(new ArrayList<>());
        }

        LiniaAlbaraProveidor linia = new LiniaAlbaraProveidor();
        linia.setLot(new LotProveidor());
        albara.getLinies().add(linia);

        model.addAttribute("albaraProveidor", albara);
        model.addAttribute("proveidors", proveidorService.findAll());
        model.addAttribute("materiesPrimeres", materiaPrimeraService.findAll());
        model.addAttribute("mode", "ocr");

        return "nou-proveidor-ocr";
    }
    /**
     * Executa l'operació processarAlbara.
     */

    @PostMapping("/albara-proveidor")
    public ResponseEntity<?> processarAlbara(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(ocrService.processarAlbara(file));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processant OCR: " + e.getMessage());
        }
    }
}
