/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.controller;

import cat.copernic.easytraza.entities.Usuari;
import cat.copernic.easytraza.service.UsuariService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author orjon Controlador web per gestionar usuaris.
 */
@Controller
public class UsuariController {

    private final UsuariService usuariService;

    public UsuariController(UsuariService usuariService) {
        this.usuariService = usuariService;
    }

    @GetMapping("/usuaris")
    public String mostrarUsuaris(Model model) {
        model.addAttribute("usuaris", usuariService.findAll());
        return "usuaris";
    }

    @GetMapping("/usuaris/nou")
    public String mostrarFormulariNouUsuari(Model model) {
        model.addAttribute("usuari", new Usuari());
        model.addAttribute("mode", "create");
        return "nou-usuari";
    }

    @PostMapping("/usuaris/guardar")
    public String guardar(@ModelAttribute Usuari usuari,
            @RequestParam(required = false) MultipartFile fotoFile,
            Model model) {
        try {
            Usuari usuariGuardat;

            if (usuari.getId() != null) {
                usuariGuardat = usuariService.update(usuari.getId(), usuari);
            } else {
                usuariGuardat = usuariService.save(usuari);
            }

            if (fotoFile != null && !fotoFile.isEmpty()) {
                String nomOriginal = fotoFile.getOriginalFilename();
                String extensio = "";

                if (nomOriginal != null && nomOriginal.contains(".")) {
                    extensio = nomOriginal.substring(nomOriginal.lastIndexOf("."));
                }

                String nomFitxer = "usuari_" + usuariGuardat.getId() + extensio;

                Path directori = Paths.get("uploads/usuaris");
                Files.createDirectories(directori);

                Path rutaFitxer = directori.resolve(nomFitxer);
                Files.write(rutaFitxer, fotoFile.getBytes());

                usuariGuardat.setFoto(nomFitxer);
                usuariService.update(usuariGuardat.getId(), usuariGuardat);
            }

            return "redirect:/usuaris";

        } catch (RuntimeException | IOException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("usuari", usuari);
            model.addAttribute("mode", usuari.getId() != null ? "edit" : "create");
            return "nou-usuari";
        }
    }

    @GetMapping("/usuaris/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Usuari usuari = usuariService.findById(id).orElse(null);

        if (usuari == null) {
            return "redirect:/usuaris";
        }

        model.addAttribute("usuari", usuari);
        model.addAttribute("mode", "edit");
        return "nou-usuari";
    }

    @GetMapping("/usuaris/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        usuariService.deleteById(id);
        return "redirect:/usuaris";
    }
}
