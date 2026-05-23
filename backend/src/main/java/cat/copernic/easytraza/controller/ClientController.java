package cat.copernic.easytraza.controller;

import cat.copernic.easytraza.entities.Client;
import cat.copernic.easytraza.service.ClientService;
import cat.copernic.easytraza.utils.ValidacioEmail;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author orjon
 */
@Controller
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;
    /**
     * Executa l'operació ClientController.
     */

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }
    /**
     * Consulta dades i retorna la informació necessària per a la vista o l'API.
     */

    @GetMapping
    public String mostrarClients(
            @RequestParam(required = false) String filtre,
            @RequestParam(required = false) String ordre,
            Model model) {

        List<Client> clients = clientService.filtrar(filtre, ordre);

        model.addAttribute("clients", clients);
        model.addAttribute("filtre", filtre);
        model.addAttribute("ordre", ordre);

        return "clients";
    }
    /**
     * Executa l'operació nouClient.
     */

    @GetMapping("/nou")
    public String nouClient(Model model) {
        model.addAttribute("client", new Client());
        model.addAttribute("mode", "create");
        return "nou-client";
    }
    /**
     * Valida i desa la informació rebuda.
     */

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Client client, Model model) {

        boolean esCreacio = client.getId() == null;

        try {
            if (client.getEmail() != null && client.getEmail().trim().isEmpty()) {
                client.setEmail(null);
            }
            if (ValidacioEmail.emailNoValid(client.getEmail())) {
                model.addAttribute("error", "El format del correu electrònic no és vàlid.");
                model.addAttribute("client", client);
                model.addAttribute("mode", esCreacio ? "create" : "edit");
                return "nou-client";
            }

            if (!esCreacio) {
                clientService.update(client.getId(), client);
            } else {
                clientService.save(client);
            }

            return "redirect:/clients";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("client", client);
            model.addAttribute("mode", esCreacio ? "create" : "edit");
            return "nou-client";
        }
    }
    /**
     * Actualitza una entitat existent amb les dades indicades.
     */

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Client client = clientService.findById(id).orElse(null);

        if (client == null) {
            return "redirect:/clients";
        }

        model.addAttribute("client", client);
        model.addAttribute("mode", "edit");
        return "nou-client";
    }
    /**
     * Consulta dades i retorna la informació necessària per a la vista o l'API.
     */

    @GetMapping("/veure/{id}")
    public String veure(@PathVariable Long id, Model model) {
        Client client = clientService.findById(id).orElse(null);

        if (client == null) {
            return "redirect:/clients";
        }

        model.addAttribute("client", client);
        model.addAttribute("mode", "view");
        return "nou-client";
    }
    /**
     * Elimina o desactiva el registre indicat segons les regles de negoci.
     */

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            String missatge = clientService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", missatge);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/clients";
    }
    /**
     * Activa el registre indicat.
     */

    @GetMapping("/activar/{id}")
    public String activar(@PathVariable Long id) {
        clientService.activar(id);
        return "redirect:/clients";
    }
}
