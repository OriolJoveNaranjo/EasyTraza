package cat.copernic.easytraza.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author orjon
 */
@RestController
@RequestMapping("/api")
public class HealthController {
    /**
     * Executa l'operació health.
     * @return 
     */

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
