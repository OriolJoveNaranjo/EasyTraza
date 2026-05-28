package cat.copernic.easytraza.controller;

import cat.copernic.easytraza.entities.AlbaraClient;
import cat.copernic.easytraza.entities.LiniaAlbaraClient;
import cat.copernic.easytraza.enums.EstatAlbaraClient;
import cat.copernic.easytraza.repository.LotProveidorRepository;
import cat.copernic.easytraza.service.AlbaraClientService;
import cat.copernic.easytraza.service.ClientService;
import cat.copernic.easytraza.service.ProducteFinalService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import cat.copernic.easytraza.enums.EstatLot;
import cat.copernic.easytraza.repository.TracabilitatRepository;

/**
 *
 * @author orjon
 */
@Controller
@RequestMapping("/albarans-client")
public class AlbaraClientController {

    private final AlbaraClientService service;
    private final ClientService clientService;
    private final ProducteFinalService producteService;
    private final LotProveidorRepository lotProveidorRepository;
    private final TracabilitatRepository tracabilitatRepository;
    /**
     * Executa l'operació AlbaraClientController.
     * @param service
     * @param clientService
     * @param producteService
     * @param tracabilitatRepository
     * @param lotProveidorRepository
     */

    public AlbaraClientController(
            AlbaraClientService service,
            ClientService clientService,
            ProducteFinalService producteService,
            TracabilitatRepository tracabilitatRepository,
            LotProveidorRepository lotProveidorRepository
    ) {
        this.service = service;
        this.clientService = clientService;
        this.producteService = producteService;
        this.lotProveidorRepository = lotProveidorRepository;
        this.tracabilitatRepository = tracabilitatRepository;
    }

    // FORM NUEVO
    /**
     * Executa l'operació nou.
     * @param model
     * @return 
     */
    @GetMapping("/nou")
    public String nou(Model model) {
        AlbaraClient a = new AlbaraClient();
        a.setData(LocalDateTime.now());
        a.setEstat(EstatAlbaraClient.PENDENT);

        a.setLinies(new ArrayList<>());
        a.getLinies().add(new LiniaAlbaraClient());

        model.addAttribute("albaraClient", a);
        model.addAttribute("clients", clientService.findAll());
        model.addAttribute("productes", producteService.findAll());
        model.addAttribute("lots", lotProveidorRepository.findByEstat(EstatLot.OBERT));
        model.addAttribute("mode", "create");

        return "nou-albara-client";
    }

    // GUARDAR
    /**
     * Valida i desa la informació rebuda.
     * @param albara
     * @return 
     */
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute AlbaraClient albara) {
        service.saveAmbTracabilitatAutomatica(albara);
        return "redirect:/albarans-client";
    }

    // EDITAR
    /**
     * Actualitza una entitat existent amb les dades indicades.
     * @param id
     * @param model
     * @return 
     */
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        AlbaraClient a = service.findById(id)
                .orElseThrow(() -> new RuntimeException("No trobat"));

        model.addAttribute("albaraClient", a);
        model.addAttribute("clients", clientService.findAll());
        model.addAttribute("productes", producteService.findAll());
        model.addAttribute("lots", lotProveidorRepository.findByEstat(EstatLot.OBERT));
        model.addAttribute("mode", "edit");

        return "nou-albara-client";
    }

    // ELIMINAR
    /**
     * Elimina o desactiva el registre indicat segons les regles de negoci.
     * @param id
     * @return 
     */
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/albarans-client";
    }
    /**
     * Executa l'operació marcarComLliurat.
     * @param id
     * @return 
     */

    @GetMapping("/lliurar/{id}")
    public String marcarComLliurat(@PathVariable Long id) {
        service.marcarComLliurat(id);
        return "redirect:/albarans-client";
    }
    /**
     * Consulta dades i retorna la informació necessària per a la vista o l'API.
     * @param id
     * @param model
     * @return 
     */

    @GetMapping("/veure/{id}")
    public String veure(@PathVariable Long id, Model model) {
        AlbaraClient albara = service.findById(id)
                .orElseThrow(() -> new RuntimeException("Albarà de client no trobat"));

        model.addAttribute("albaraClient", albara);
        model.addAttribute("clients", clientService.findAll());
        model.addAttribute("productes", producteService.findAll());
        model.addAttribute("lots", lotProveidorRepository.findByEstat(EstatLot.OBERT));
        model.addAttribute("tracabilitats", tracabilitatRepository.findByLiniaAlbaraClient_AlbaraClient_Id(id));
        model.addAttribute("mode", "view");

        return "nou-albara-client";
    }
    /**
     * Consulta dades i retorna la informació necessària per a la vista o l'API.
     * @param clientId
     * @param estat
     * @param ordre
     * @param model
     * @return 
     */

    @GetMapping
    public String llistar(
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) String estat,
            @RequestParam(required = false) String ordre,
            Model model) {

        List<AlbaraClient> albarans = service.filtrar(clientId, estat, ordre);

        model.addAttribute("albarans", albarans);
        model.addAttribute("clients", clientService.findAll());
        model.addAttribute("clientId", clientId);
        model.addAttribute("estatSeleccionat", estat);
        model.addAttribute("ordre", ordre);

        return "albarans-client";
    }
}
