package cat.copernic.easytraza.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author orjon Controlador para mostrar la pantalla de login.
 */
@Controller
public class LoginController {
    /**
     * Executa l'operació login.
     * @return 
     */

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
