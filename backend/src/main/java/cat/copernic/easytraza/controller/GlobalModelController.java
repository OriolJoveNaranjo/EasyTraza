/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.controller;

import cat.copernic.easytraza.entities.Usuari;
import cat.copernic.easytraza.repository.UsuariRepository;
import java.security.Principal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 *
 * @author orjon Añade datos globales disponibles en todas las vistas Thymeleaf.
 */
@ControllerAdvice
public class GlobalModelController {

    private final UsuariRepository usuariRepository;

    public GlobalModelController(UsuariRepository usuariRepository) {
        this.usuariRepository = usuariRepository;
    }

    @ModelAttribute("usuariSessio")
    public Usuari usuariSessio(Principal principal) {
        if (principal == null) {
            return null;
        }

        return usuariRepository.findByEmail(principal.getName())
                .orElse(null);
    }
}
