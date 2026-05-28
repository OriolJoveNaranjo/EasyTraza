package cat.copernic.easytraza.controller;

import cat.copernic.easytraza.enums.EstatAlbaraClient;
import cat.copernic.easytraza.repository.LiniaAlbaraClientRepository;
import cat.copernic.easytraza.repository.ProducteFinalRepository;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author orjon RF23 - Gràfic mensual de productes venuts.
 */
@Controller
public class GraficProductesController {

    private final LiniaAlbaraClientRepository liniaRepository;
    private final ProducteFinalRepository producteRepository;
    /**
     * Executa l'operació GraficProductesController.
     * @param liniaRepository
     * @param producteRepository
     */

    public GraficProductesController(LiniaAlbaraClientRepository liniaRepository,
            ProducteFinalRepository producteRepository) {
        this.liniaRepository = liniaRepository;
        this.producteRepository = producteRepository;
    }
    /**
     * Executa l'operació graficProductes.
     * @param mes
     * @param producteId
     * @param model
     * @return 
     */

    @GetMapping("/informes/productes-mensual")
    public String graficProductes(
            @RequestParam(required = false) String mes,
            @RequestParam(required = false) Long producteId,
            Model model) {

        YearMonth mesSeleccionat = mes != null && !mes.isBlank()
                ? YearMonth.parse(mes)
                : YearMonth.now();

        LocalDateTime inici = mesSeleccionat.atDay(1).atStartOfDay();
        LocalDateTime fi = mesSeleccionat.plusMonths(1).atDay(1).atStartOfDay();

        int diesMes = mesSeleccionat.lengthOfMonth();

        List<Integer> dies = new ArrayList<>();
        List<Double> quantitats = new ArrayList<>();

        for (int i = 1; i <= diesMes; i++) {
            dies.add(i);
            quantitats.add(0.0);
        }

        List<Object[]> dades = liniaRepository.vendesPerDia(
                inici,
                fi,
                producteId,
                EstatAlbaraClient.LLIURAT
        );

        for (Object[] fila : dades) {
            Integer dia = ((Number) fila[0]).intValue();
            Double total = ((Number) fila[1]).doubleValue();

            quantitats.set(dia - 1, total);
        }

        double totalMes = quantitats.stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        List<YearMonth> ultims12Mesos = new ArrayList<>();
        YearMonth actual = YearMonth.now();

        for (int i = 0; i < 12; i++) {
            ultims12Mesos.add(actual.minusMonths(i));
        }

        model.addAttribute("mesos", ultims12Mesos);
        model.addAttribute("mesSeleccionat", mesSeleccionat.toString());
        model.addAttribute("productes", producteRepository.findAll());
        model.addAttribute("producteSeleccionat", producteId);
        model.addAttribute("dies", dies);
        model.addAttribute("quantitats", quantitats);
        model.addAttribute("totalMes", totalMes);

        return "grafic-productes";
    }
}
