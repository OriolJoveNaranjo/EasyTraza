package cat.copernic.easytraza.controller;

import cat.copernic.easytraza.entities.MateriaPrimera;
import cat.copernic.easytraza.service.MateriaPrimeraService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author orjon
 */
@Controller
public class MateriaPrimeraController {

    private final MateriaPrimeraService materiaPrimeraService;

    public MateriaPrimeraController(MateriaPrimeraService materiaPrimeraService) {
        this.materiaPrimeraService = materiaPrimeraService;
    }

    @GetMapping("/cataleg")
    public String mostrarCataleg(
            @RequestParam(required = false) String nom,
            Model model) {

        List<MateriaPrimera> materiesPrimeres = materiaPrimeraService.findAll();

        if (nom != null && !nom.isBlank()) {
            String nomLower = nom.toLowerCase();

            materiesPrimeres = materiesPrimeres.stream()
                    .filter(materia -> materia.getNom() != null
                    && materia.getNom().toLowerCase().contains(nomLower))
                    .toList();
        }

        model.addAttribute("materiesPrimeres", materiesPrimeres);
        model.addAttribute("nom", nom);

        return "cataleg";
    }

    @GetMapping("/cataleg/materies-primeres/nova")
    public String mostrarFormulariNovaMateriaPrimera(Model model) {
        model.addAttribute("materiaPrimera", new MateriaPrimera());
        return "nova-materia-primera";
    }

    @PostMapping("/cataleg/materies-primeres/guardar")
    public String guardar(@ModelAttribute MateriaPrimera materiaPrimera, Model model) {

        try {
            if (materiaPrimera.getId() != null) {
                materiaPrimeraService.update(materiaPrimera.getId(), materiaPrimera);
            } else {
                materiaPrimeraService.save(materiaPrimera);
            }

            return "redirect:/cataleg";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("materiaPrimera", materiaPrimera);
            return "nova-materia-primera";
        }
    }

    @GetMapping("/cataleg/materies-primeres/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            String missatge = materiaPrimeraService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", missatge);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/cataleg";
    }

    @GetMapping("/cataleg/materies-primeres/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        MateriaPrimera materia = materiaPrimeraService.findById(id).orElse(null);

        if (materia == null) {
            return "redirect:/cataleg";
        }

        model.addAttribute("materiaPrimera", materia);
        return "nova-materia-primera";
    }

    @GetMapping("/cataleg/activar/{id}")
    public String activar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        materiaPrimeraService.activar(id);
        redirectAttributes.addFlashAttribute("success", "Matèria primera activada correctament.");
        return "redirect:/cataleg";
    }

}
