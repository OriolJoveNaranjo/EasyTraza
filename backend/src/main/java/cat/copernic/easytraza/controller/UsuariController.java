package cat.copernic.easytraza.controller;

import cat.copernic.easytraza.entities.Usuari;
import cat.copernic.easytraza.service.UsuariService;
import jakarta.validation.Valid;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import cat.copernic.easytraza.utils.ValidacioEmail;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;

/**
 *
 * @author orjon Controlador web per gestionar usuaris.
 */
@Controller
public class UsuariController {

    private final UsuariService usuariService;

    @Value("${app.superadmin.email}")
    private String superAdminEmail;
    /**
     * Executa l'operació UsuariController.
     * @param usuariService
     */

    public UsuariController(UsuariService usuariService) {
        this.usuariService = usuariService;
    }
    /**
     * Consulta dades i retorna la informació necessària per a la vista o l'API.
     * @param nom
     * @param rol
     * @param model
     * @param authentication
     * @return 
     */

    @GetMapping("/usuaris")
    public String mostrarUsuaris(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String rol,
            Model model,
            Authentication authentication) {

        List<Usuari> usuaris = usuariService.findAll();

        if (nom != null && !nom.isBlank()) {
            String nomLower = nom.toLowerCase();
            usuaris = usuaris.stream()
                    .filter(u -> u.getNom() != null && u.getNom().toLowerCase().contains(nomLower))
                    .toList();
        }

        if (rol != null && !rol.isBlank()) {
            usuaris = usuaris.stream()
                    .filter(u -> u.getRol() != null && u.getRol().name().equals(rol))
                    .toList();
        }

        model.addAttribute("usuaris", usuaris);
        model.addAttribute("nom", nom);
        model.addAttribute("rol", rol);
        model.addAttribute("emailActual", authentication.getName());
        model.addAttribute("superAdminEmail", superAdminEmail);

        return "usuaris";
    }
    /**
     * Consulta dades i retorna la informació necessària per a la vista o l'API.
     * @param model
     * @return 
     */

    @GetMapping("/usuaris/nou")
    public String mostrarFormulariNouUsuari(Model model) {
        model.addAttribute("usuari", new Usuari());
        model.addAttribute("mode", "create");
        return "nou-usuari";
    }
    /**
     * Valida i desa la informació rebuda.
     * @param usuari
     * @param result
     * @param fotoFile
     * @param model
     * @return 
     */

    @PostMapping("/usuaris/guardar")
    public String guardar(@Valid @ModelAttribute Usuari usuari,
            BindingResult result,
            @RequestParam(required = false) MultipartFile fotoFile,
            Model model) {

        boolean esCreacio = usuari.getId() == null;

        try {
            if (esCreacio && ValidacioEmail.emailNoValid(usuari.getEmail())) {
                model.addAttribute("error", "El format del correu electrònic no és vàlid.");
                model.addAttribute("usuari", usuari);
                model.addAttribute("mode", "create");
                return "nou-usuari";
            }

            Usuari usuariGuardat;

            if (!esCreacio) {
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
            model.addAttribute("mode", esCreacio ? "create" : "edit");
            return "nou-usuari";
        }
    }
    /**
     * Actualitza una entitat existent amb les dades indicades.
     * @param id
     * @param model
     * @return 
     */

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
    /**
     * Elimina o desactiva el registre indicat segons les regles de negoci.
     * @param id
     * @param redirectAttributes
     * @return 
     */

    @GetMapping("/usuaris/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            String missatge = usuariService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", missatge);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/usuaris";
    }
    /**
     * Activa el registre indicat.
     * @param id
     * @param redirectAttributes
     * @return 
     */

    @GetMapping("/usuaris/activar/{id}")
    public String activar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        usuariService.activar(id);
        redirectAttributes.addFlashAttribute("success", "Usuari activat correctament.");
        return "redirect:/usuaris";
    }
}
