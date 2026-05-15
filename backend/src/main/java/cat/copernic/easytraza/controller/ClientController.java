/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.controller;

import cat.copernic.easytraza.entities.Client;
import cat.copernic.easytraza.service.ClientService;
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

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

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

    @GetMapping("/nou")
    public String nouClient(Model model) {
        model.addAttribute("client", new Client());
        model.addAttribute("mode", "create");
        return "nou-client";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Client client, Model model) {
        try {
            if (client.getId() != null) {
                clientService.update(client.getId(), client);
            } else {
                clientService.save(client);
            }

            return "redirect:/clients";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("client", client);
            model.addAttribute("mode", client.getId() != null ? "edit" : "create");
            return "nou-client";
        }
    }

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

    @GetMapping("/activar/{id}")
    public String activar(@PathVariable Long id) {
        clientService.activar(id);
        return "redirect:/clients";
    }
}
