/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.controller;

import cat.copernic.easytraza.entities.AlbaraProveidor;
import cat.copernic.easytraza.entities.FitxerAlbaraProveidor;
import cat.copernic.easytraza.entities.LiniaAlbaraProveidor;
import cat.copernic.easytraza.entities.LotProveidor;
import cat.copernic.easytraza.repository.FitxerAlbaraProveidorRepository;
import cat.copernic.easytraza.service.AlbaraProveidorService;
import cat.copernic.easytraza.service.MateriaPrimeraService;
import cat.copernic.easytraza.service.ProveidorService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author orjon
 */
@Controller
public class AlbaraProveidorController {

    private static final Logger logger = LoggerFactory.getLogger(AlbaraProveidorController.class);

    private final AlbaraProveidorService albaraProveidorService;
    private final ProveidorService proveidorService;
    private final MateriaPrimeraService materiaPrimeraService;
    private final FitxerAlbaraProveidorRepository fitxerAlbaraProveidorRepository;
    /**
     * Executa l'operació AlbaraProveidorController.
     */

    public AlbaraProveidorController(
            AlbaraProveidorService albaraProveidorService,
            ProveidorService proveidorService,
            MateriaPrimeraService materiaPrimeraService,
            FitxerAlbaraProveidorRepository fitxerAlbaraProveidorRepository
    ) {
        this.albaraProveidorService = albaraProveidorService;
        this.proveidorService = proveidorService;
        this.materiaPrimeraService = materiaPrimeraService;
        this.fitxerAlbaraProveidorRepository = fitxerAlbaraProveidorRepository;
    }
    /**
     * Consulta dades i retorna la informació necessària per a la vista o l'API.
     */

    @GetMapping("/albarans-proveidor")
    public String mostrarAlbaransProveidor(
            @RequestParam(required = false) Long proveidorId,
            @RequestParam(required = false) String ordre,
            Model model) {

        List<AlbaraProveidor> albarans = albaraProveidorService.filtrar(proveidorId, ordre);

        model.addAttribute("albarans", albarans);
        model.addAttribute("proveidors", proveidorService.findAll());
        model.addAttribute("proveidorId", proveidorId);
        model.addAttribute("ordre", ordre);

        return "albarans-proveidor";
    }
    /**
     * Consulta dades i retorna la informació necessària per a la vista o l'API.
     */

    @GetMapping("/albarans-proveidor/nou")
    public String mostrarFormulariNou(Model model) {
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
        model.addAttribute("mode", "create");

        return "nou-albara-proveidor";
    }
    /**
     * Valida i desa la informació rebuda.
     */

    @PostMapping("/albarans-proveidor/guardar")
    public String guardar(
            @ModelAttribute AlbaraProveidor albaraProveidor,
            @RequestParam(name = "formSource", defaultValue = "manual") String formSource,
            @RequestParam(name = "ocrTextHidden", required = false) String ocrTextHidden,
            @RequestParam(value = "fitxersPujats", required = false) MultipartFile[] fitxers,
            Model model) {

        try {
            logger.info("Fitxers rebuts en formulari d\'albarà de proveïdor: {}", fitxers == null ? "null" : fitxers.length);

            if (fitxers != null) {
                for (MultipartFile f : fitxers) {
                    logger.debug("Fitxer rebut: {} / buit={}", f.getOriginalFilename(), f.isEmpty());
                }
            }

            if (albaraProveidor.getId() != null) {
                albaraProveidorService.update(albaraProveidor.getId(), albaraProveidor, fitxers);
            } else {
                albaraProveidorService.save(albaraProveidor, fitxers);
            }

            return "redirect:/albarans-proveidor";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("albaraProveidor", albaraProveidor);
            model.addAttribute("proveidors", proveidorService.findAll());
            model.addAttribute("materiesPrimeres", materiaPrimeraService.findAll());

            if ("ocr".equals(formSource)) {
                model.addAttribute("mode", "ocr");
                model.addAttribute("ocrTextHidden", ocrTextHidden);
                model.addAttribute("error", e.getMessage() + ". Torna a seleccionar la imatge abans de guardar.");
                return "nou-albara-proveidor-ocr";
            }
            

            model.addAttribute("mode", albaraProveidor.getId() != null ? "edit" : "create");
            return "nou-albara-proveidor";
        }
    }
    /**
     * Actualitza una entitat existent amb les dades indicades.
     */

    @GetMapping("/albarans-proveidor/editar/{id}")
    public String editar(@PathVariable Long id, Model model
    ) {
        AlbaraProveidor albara = albaraProveidorService.findById(id).orElse(null);

        if (albara == null) {
            return "redirect:/albarans-proveidor";
        }

        if (albara.getLinies() == null) {
            albara.setLinies(new ArrayList<>());
        }

        if (albara.getLinies().isEmpty()) {
            LiniaAlbaraProveidor linia = new LiniaAlbaraProveidor();
            linia.setLot(new LotProveidor());
            albara.getLinies().add(linia);
        }

        for (LiniaAlbaraProveidor linia : albara.getLinies()) {
            if (linia.getLot() == null) {
                linia.setLot(new LotProveidor());
            }
        }

        model.addAttribute("albaraProveidor", albara);
        model.addAttribute("proveidors", proveidorService.findAll());
        model.addAttribute("materiesPrimeres", materiaPrimeraService.findAll());
        model.addAttribute("mode", "edit");

        return "nou-albara-proveidor";
    }
    /**
     * Consulta dades i retorna la informació necessària per a la vista o l'API.
     */

    @GetMapping("/albarans-proveidor/veure/{id}")
    public String veure(@PathVariable Long id, Model model
    ) {
        AlbaraProveidor albara = albaraProveidorService.findById(id).orElse(null);

        if (albara == null) {
            return "redirect:/albarans-proveidor";
        }

        model.addAttribute("albaraProveidor", albara);
        model.addAttribute("mode", "view");

        return "nou-albara-proveidor";
    }
    /**
     * Elimina o desactiva el registre indicat segons les regles de negoci.
     */

    @GetMapping("/albarans-proveidor/eliminar/{id}")
    public String eliminar(@PathVariable Long id
    ) {
        albaraProveidorService.deleteById(id);
        return "redirect:/albarans-proveidor";
    }

    private void netejarLiniesBuides(AlbaraProveidor albaraProveidor) {
        if (albaraProveidor.getLinies() == null) {
            albaraProveidor.setLinies(new ArrayList<>());
            return;
        }

        List<LiniaAlbaraProveidor> liniesNetes = new ArrayList<>();

        for (LiniaAlbaraProveidor linia : albaraProveidor.getLinies()) {
            if (linia == null) {
                continue;
            }
            if (linia.getMateriaPrimera() == null) {
                continue;
            }
            if (linia.getLot() == null) {
                continue;
            }
            if (linia.getLot().getIdentificadorLot() == null || linia.getLot().getIdentificadorLot().isBlank()) {
                continue;
            }
            if (linia.getQuantitat() == null || linia.getQuantitat() <= 0) {
                continue;
            }

            linia.getLot().setAlbaraProveidor(albaraProveidor);
            linia.getLot().setMateriaPrimera(linia.getMateriaPrimera());
            linia.getLot().setProveidor(albaraProveidor.getProveidor());
            linia.getLot().setQuantitat(linia.getQuantitat());
            linia.getLot().setUnitat(linia.getUnitat());

            liniesNetes.add(linia);
        }

        albaraProveidor.setLinies(liniesNetes);
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

        return "nou-albara-proveidor-ocr";
    }
    /**
     * Consulta dades i retorna la informació necessària per a la vista o l'API.
     */

    @GetMapping("/albarans-proveidor/fitxer/{id}")
    public ResponseEntity<byte[]> veureFitxer(@PathVariable Long id) {
        return fitxerAlbaraProveidorRepository.findById(id)
                .map(fitxer -> ResponseEntity.ok()
                .header("Content-Disposition", "inline; filename=\"" + fitxer.getNomFitxer() + "\"")
                .contentType(MediaType.parseMediaType(fitxer.getTipusFitxer()))
                .body(fitxer.getDades()))
                .orElse(ResponseEntity.notFound().build());
    }
}
