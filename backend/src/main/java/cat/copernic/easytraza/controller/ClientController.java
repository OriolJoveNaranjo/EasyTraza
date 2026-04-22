/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.controller;

import cat.copernic.easytraza.entities.Client;
import cat.copernic.easytraza.service.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author orjon
 */
@Controller
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/clients")
    public String mostrarClients(Model model) {
        model.addAttribute("clients", clientService.findAll());
        return "clients";
    }

    @GetMapping("/clients/nou")
    public String nouClient(Model model) {
        model.addAttribute("client", new Client());
        model.addAttribute("mode", "create");
        return "nou-client";
    }

    @PostMapping("/clients/guardar")
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

    @GetMapping("/clients/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Client client = clientService.findById(id).orElse(null);

        if (client == null) {
            return "redirect:/clients";
        }

        model.addAttribute("client", client);
        model.addAttribute("mode", "edit");
        return "nou-client";
    }

    @GetMapping("/clients/veure/{id}")
    public String veure(@PathVariable Long id, Model model) {
        Client client = clientService.findById(id).orElse(null);

        if (client == null) {
            return "redirect:/clients";
        }

        model.addAttribute("client", client);
        model.addAttribute("mode", "view");
        return "nou-client";
    }

    @GetMapping("/clients/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        clientService.deleteById(id);
        return "redirect:/clients";
    }
}
