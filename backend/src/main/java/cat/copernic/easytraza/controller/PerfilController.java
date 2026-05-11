/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.controller;

import cat.copernic.easytraza.entities.Usuari;
import cat.copernic.easytraza.repository.UsuariRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author orjon Controlador para editar el perfil del usuario autenticado.
 */
@Controller
public class PerfilController {

    private final UsuariRepository usuariRepository;
    private final PasswordEncoder passwordEncoder;

    public PerfilController(UsuariRepository usuariRepository, PasswordEncoder passwordEncoder) {
        this.usuariRepository = usuariRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/perfil")
    public String mostrarPerfil(Model model, Principal principal) {
        Usuari usuari = usuariRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuari no trobat"));

        model.addAttribute("usuari", usuari);

        return "perfil";
    }

    @PostMapping("/perfil")
    public String guardarPerfil(
            @RequestParam String nom,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) MultipartFile foto,
            Principal principal) throws IOException {

        Usuari usuari = usuariRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuari no trobat"));

        usuari.setNom(nom);

        if (password != null && !password.isBlank()) {
            usuari.setPassword(passwordEncoder.encode(password));
        }

        if (foto != null && !foto.isEmpty()) {
            String nomOriginal = foto.getOriginalFilename();
            String extensio = "";

            if (nomOriginal != null && nomOriginal.contains(".")) {
                extensio = nomOriginal.substring(nomOriginal.lastIndexOf("."));
            }

            String nomFitxer = "usuari_" + usuari.getId() + extensio;

            Path directori = Paths.get("uploads/usuaris");
            Files.createDirectories(directori);

            Path rutaFitxer = directori.resolve(nomFitxer);
            Files.write(rutaFitxer, foto.getBytes());

            usuari.setFoto(nomFitxer);
        }

        usuariRepository.save(usuari);

        return "redirect:/perfil?success";
    }
}
