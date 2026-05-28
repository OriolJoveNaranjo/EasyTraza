package cat.copernic.easytraza.controller.api;

import cat.copernic.easytraza.entities.Proveidor;
import cat.copernic.easytraza.service.ProveidorService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author orjon
 * 
 * Controlador web per gestionar proveïdors.
 */

@RestController
@RequestMapping("/api/proveidors")
public class ProveidorRestController {

    private final ProveidorService proveidorService;
    /**
     * Executa l'operació ProveidorRestController.
     * @param proveidorService
     */

    public ProveidorRestController(ProveidorService proveidorService) {
        this.proveidorService = proveidorService;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    @GetMapping
    public ResponseEntity<List<Proveidor>> getAll() {
        return ResponseEntity.ok(proveidorService.findAll());
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @param id
     * @return 
     */

    @GetMapping("/{id}")
    public ResponseEntity<Proveidor> getById(@PathVariable Long id) {
        Optional<Proveidor> proveidor = proveidorService.findById(id);
        return proveidor.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    /**
     * Valida i desa la informació rebuda.
     * @param proveidor
     * @return 
     */

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Proveidor proveidor) {
        try {
            Proveidor creat = proveidorService.save(proveidor);
            return ResponseEntity.ok(creat);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    /**
     * Actualitza una entitat existent amb les dades indicades.
     * @param id
     * @param proveidor
     * @return 
     */

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Proveidor proveidor) {
        try {
            Proveidor actualitzat = proveidorService.update(id, proveidor);
            return ResponseEntity.ok(actualitzat);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    /**
     * Elimina o desactiva el registre indicat segons les regles de negoci.
     * @param id
     * @return 
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Proveidor> proveidor = proveidorService.findById(id);

        if (proveidor.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        proveidorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}