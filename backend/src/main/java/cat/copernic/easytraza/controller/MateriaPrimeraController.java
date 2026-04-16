package cat.copernic.easytraza.controller;

import cat.copernic.easytraza.entities.MateriaPrimera;
import cat.copernic.easytraza.service.MateriaPrimeraService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String mostrarCataleg(Model model) {
        model.addAttribute("materiesPrimeres", materiaPrimeraService.findAll());
        return "/cataleg";
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
    public String eliminar(@PathVariable Long id) {
        materiaPrimeraService.deleteById(id);
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

}
