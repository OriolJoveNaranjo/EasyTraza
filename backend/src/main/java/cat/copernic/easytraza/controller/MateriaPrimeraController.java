
package cat.copernic.easytraza.controller;

import cat.copernic.easytraza.entities.MateriaPrimera;
import cat.copernic.easytraza.service.MateriaPrimeraService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

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
    public RedirectView guardarMateriaPrimera(@ModelAttribute MateriaPrimera materiaPrimera) {
        materiaPrimeraService.save(materiaPrimera);
        return new RedirectView("/cataleg");
    }
}

