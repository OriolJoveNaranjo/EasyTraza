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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * API mòbil per consultar proveïdors i registrar albarans de proveïdor des de
 * l'aplicació Android.
 *
 * <p>Aquest controlador transforma les peticions DTO del mòbil en entitats de
 * domini i reutilitza el servei d'albarans perquè les dades quedin disponibles
 * també a la part web.</p>
 *
 * @author orjon
 */
@RestController
@RequestMapping("/api/mobile/albarans-proveidor")
public class MobileAlbaraProveidorApiController {

    private static final Logger logger = LoggerFactory.getLogger(MobileAlbaraProveidorApiController.class);

    private final AlbaraProveidorService albaraProveidorService;
    private final ProveidorService proveidorService;
    private final MateriaPrimeraService materiaPrimeraService;
    private final UsuariService usuariService;

    /**
     * Crea el controlador API per a albarans de proveïdor del mòbil.
     *
     * @param albaraProveidorService servei d'albarans de proveïdor
     * @param proveidorService servei de proveïdors
     * @param materiaPrimeraService servei de matèries primeres
     * @param usuariService servei d'usuaris
     */
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

    /**
     * Retorna els proveïdors actius disponibles per al formulari mòbil.
     *
     * @return llista de proveïdors actius
     */
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

    /**
     * Guarda un albarà rebut des del mòbil amb els seus lots associats.
     *
     * @param request dades de l'albarà i dels lots introduïts al dispositiu
     * @return resposta HTTP amb missatge d'èxit o error de validació
     */
    @PostMapping("/guardar")
    public ResponseEntity<String> guardar(
            @RequestBody GuardarAlbaraMobileRequest request
    ) {
        try {
            logger.info("Rebuda petició mòbil per guardar albarà {}", request.numeroAlbara());

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
            logger.info("Albarà {} guardat correctament des del mòbil", request.numeroAlbara());

            return ResponseEntity.ok("Albarà guardat correctament");

        } catch (RuntimeException e) {
            logger.warn("No s'ha pogut guardar l'albarà rebut des del mòbil: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Dades mínimes d'un proveïdor per al selector del mòbil.
     *
     * @param id identificador del proveïdor
     * @param nom nom visible del proveïdor
     */
    public record ProveidorMobileDto(
            Long id,
            String nom
            ) {

    }

    /**
     * Petició de guardat d'albarà enviada pel mòbil.
     *
     * @param numeroAlbara número d'albarà
     * @param proveidor nom del proveïdor seleccionat
     * @param dataRecepcio data de recepció en format ISO
     * @param observacions observacions opcionals
     * @param usuariId identificador de l'usuari seleccionat al mòbil
     * @param lots lots inclosos a l'albarà
     */
    public record GuardarAlbaraMobileRequest(
            String numeroAlbara,
            String proveidor,
            String dataRecepcio,
            String observacions,
            Long usuariId,
            List<GuardarLotMobileRequest> lots
            ) {

    }

    /**
     * Dades d'un lot enviat pel mòbil dins d'un albarà.
     *
     * @param materiaPrimera nom de la matèria primera
     * @param quantitat quantitat rebuda
     * @param unitat unitat de mesura
     * @param identificadorLot identificador del lot
     * @param dataCaducitat data de caducitat en format ISO
     */
    public record GuardarLotMobileRequest(
            String materiaPrimera,
            Double quantitat,
            String unitat,
            String identificadorLot,
            String dataCaducitat
            ) {

    }
}
