package cat.copernic.easytraza.controller.api;

import cat.copernic.easytraza.entities.AlbaraProveidor;
import cat.copernic.easytraza.entities.LiniaAlbaraProveidor;
import cat.copernic.easytraza.entities.LotProveidor;
import cat.copernic.easytraza.entities.MateriaPrimera;
import cat.copernic.easytraza.entities.Proveidor;
import cat.copernic.easytraza.entities.Usuari;
import cat.copernic.easytraza.service.AlbaraProveidorService;
import cat.copernic.easytraza.service.MateriaPrimeraService;
import cat.copernic.easytraza.service.ProveidorService;
import cat.copernic.easytraza.service.UsuariService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mobile/albarans-proveidor")
public class MobileAlbaraProveidorApiController {

    private final AlbaraProveidorService albaraProveidorService;
    private final ProveidorService proveidorService;
    private final MateriaPrimeraService materiaPrimeraService;
    private final UsuariService usuariService;

    public MobileAlbaraProveidorApiController(
            AlbaraProveidorService albaraProveidorService,
            ProveidorService proveidorService,
            MateriaPrimeraService materiaPrimeraService,
            UsuariService usuariService
    ) {
        this.albaraProveidorService = albaraProveidorService;
        this.proveidorService = proveidorService;
        this.materiaPrimeraService = materiaPrimeraService;
        this.usuariService = usuariService;
    }

    @GetMapping("/proveidors")
    public List<ProveidorMobileDto> getProveidors() {
        return proveidorService.findAll()
                .stream()
                .filter(Proveidor::isActiu)
                .map(p -> new ProveidorMobileDto(
                p.getId(),
                p.getNom()
        ))
                .toList();
    }

    @PostMapping("/guardar")
    public ResponseEntity<String> guardar(
            @RequestBody GuardarAlbaraMobileRequest request
    ) {
        try {
            System.out.println("ENTRA A GUARDAR ALBARÀ MOBILE");
            System.out.println(request);

            Proveidor proveidor = proveidorService.findAll()
                    .stream()
                    .filter(p -> p.getNom().equalsIgnoreCase(request.proveidor()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("El proveïdor no existeix"));

            Usuari usuari = usuariService.findById(request.usuariId())
                    .orElseThrow(() -> new RuntimeException("Usuari no trobat amb id: " + request.usuariId()));

            AlbaraProveidor albara = new AlbaraProveidor();
            albara.setNumeroAlbara(request.numeroAlbara());
            albara.setDataRecepcio(LocalDate.parse(request.dataRecepcio()));
            albara.setProveidor(proveidor);
            albara.setUsuariAlta(usuari);
            albara.setLinies(new ArrayList<>());

            for (GuardarLotMobileRequest lotRequest : request.lots()) {
                MateriaPrimera materia = materiaPrimeraService.findAll()
                        .stream()
                        .filter(m -> m.getNom().equalsIgnoreCase(lotRequest.materiaPrimera()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("La matèria primera no existeix"));

                LotProveidor lot = new LotProveidor();
                lot.setIdentificadorLot(lotRequest.identificadorLot());
                lot.setDataCaducitat(LocalDate.parse(lotRequest.dataCaducitat()));

                LiniaAlbaraProveidor linia = new LiniaAlbaraProveidor();
                linia.setAlbaraProveidor(albara);
                linia.setMateriaPrimera(materia);
                linia.setQuantitat(lotRequest.quantitat());
                linia.setUnitat(lotRequest.unitat());
                linia.setLot(lot);

                albara.getLinies().add(linia);
            }

            albaraProveidorService.save(albara);

            return ResponseEntity.ok("Albarà guardat correctament");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public record ProveidorMobileDto(
            Long id,
            String nom
            ) {

    }

    public record GuardarAlbaraMobileRequest(
            String numeroAlbara,
            String proveidor,
            String dataRecepcio,
            String observacions,
            Long usuariId,
            List<GuardarLotMobileRequest> lots
            ) {

    }

    public record GuardarLotMobileRequest(
            String materiaPrimera,
            Double quantitat,
            String unitat,
            String identificadorLot,
            String dataCaducitat
            ) {

    }
}
