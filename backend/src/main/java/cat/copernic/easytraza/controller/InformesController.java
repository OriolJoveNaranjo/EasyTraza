package cat.copernic.easytraza.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author orjon
 */
@Controller
public class InformesController {
    /**
     * Executa l'operació informes.
     * @return 
     */

    @GetMapping("/informes")
    public String informes() {
        return "informes";
    }
}
