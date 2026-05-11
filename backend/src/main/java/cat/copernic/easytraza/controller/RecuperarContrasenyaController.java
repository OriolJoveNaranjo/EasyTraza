package cat.copernic.easytraza.controller;

import cat.copernic.easytraza.entities.PasswordResetToken;
import cat.copernic.easytraza.entities.Usuari;
import cat.copernic.easytraza.repository.UsuariRepository;
import cat.copernic.easytraza.service.PasswordResetService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import cat.copernic.easytraza.service.EmailService;

/**
 * @author orjon Controlador para recuperar y restablecer la contraseña de forma
 * segura.
 */
@Controller
public class RecuperarContrasenyaController {

    private final UsuariRepository usuariRepository;
    private final PasswordResetService passwordResetService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public RecuperarContrasenyaController(
            UsuariRepository usuariRepository,
            PasswordResetService passwordResetService,
            PasswordEncoder passwordEncoder,
            EmailService emailService) {
        this.usuariRepository = usuariRepository;
        this.passwordResetService = passwordResetService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @GetMapping("/recuperar-contrasenya")
    public String mostrarFormulariRecuperacio() {
        return "recuperar-contrasenya";
    }

    @PostMapping("/recuperar-contrasenya")
    public String enviarEnllacRecuperacio(String email, Model model) {
        Usuari usuari = usuariRepository.findByEmail(email).orElse(null);

        if (usuari != null) {
            String token = passwordResetService.crearToken(usuari);

            String enllac = "http://localhost:8080/restablir-contrasenya?token=" + token;

            emailService.enviarEnllacRecuperacio(usuari.getEmail(), enllac);
        }

        model.addAttribute("success",
                "Si el correu existeix, s'ha generat un enllaç de recuperació.");

        return "recuperar-contrasenya";
    }

    @GetMapping("/restablir-contrasenya")
    public String mostrarFormulariRestablir(@RequestParam String token, Model model) {
        try {
            passwordResetService.validarToken(token);
            model.addAttribute("token", token);
            return "restablir-contrasenya";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @PostMapping("/restablir-contrasenya")
    public String restablirContrasenya(
            @RequestParam String token,
            @RequestParam String novaContrasenya,
            Model model) {

        try {
            PasswordResetToken resetToken = passwordResetService.validarToken(token);

            Usuari usuari = resetToken.getUsuari();
            usuari.setPassword(passwordEncoder.encode(novaContrasenya));
            usuariRepository.save(usuari);

            passwordResetService.marcarComUtilitzat(resetToken);

            return "redirect:/login?passwordChanged";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }
}
