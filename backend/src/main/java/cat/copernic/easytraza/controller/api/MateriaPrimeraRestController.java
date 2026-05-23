package cat.copernic.easytraza.controller.api;

import cat.copernic.easytraza.entities.MateriaPrimera;
import cat.copernic.easytraza.service.MateriaPrimeraService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author orjon
 */
@RestController
@RequestMapping("/api/materies-primeres")
public class MateriaPrimeraRestController {

    private final MateriaPrimeraService materiaPrimeraService;
    /**
     * Executa l'operació MateriaPrimeraRestController.
     */

    public MateriaPrimeraRestController(MateriaPrimeraService materiaPrimeraService) {
        this.materiaPrimeraService = materiaPrimeraService;
    }

    /**
     * Retorna totes les matèries primeres.
     *
     * @return llista de matèries primeres
     */
    @GetMapping
    public ResponseEntity<List<MateriaPrimera>> getAll() {
        return ResponseEntity.ok(materiaPrimeraService.findAll());
    }

    /**
     * Retorna una matèria primera pel seu id.
     *
     * @param id identificador de la matèria primera
     * @return matèria primera trobada o 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<MateriaPrimera> getById(@PathVariable Long id) {
        Optional<MateriaPrimera> materiaPrimera = materiaPrimeraService.findById(id);
        return materiaPrimera.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Dona d'alta una nova matèria primera.
     *
     * @param materiaPrimera dades de la matèria primera
     * @return matèria primera creada
     */
    @PostMapping
    public ResponseEntity<MateriaPrimera> create(@Valid @RequestBody MateriaPrimera materiaPrimera) {
        MateriaPrimera creada = materiaPrimeraService.save(materiaPrimera);
        return ResponseEntity.ok(creada);
    }

    /**
     * Actualitza una matèria primera existent.
     *
     * @param id identificador de la matèria primera
     * @param materiaPrimera noves dades
     * @return matèria primera actualitzada o 404
     */
    @PutMapping("/{id}")
    public ResponseEntity<MateriaPrimera> update(
            @PathVariable Long id,
            @Valid @RequestBody MateriaPrimera materiaPrimera) {

        MateriaPrimera actualitzada = materiaPrimeraService.update(id, materiaPrimera);

        if (actualitzada == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(actualitzada);
    }

    /**
     * Elimina una matèria primera pel seu id.
     *
     * @param id identificador de la matèria primera
     * @return 204 si s'elimina, 404 si no existeix
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<MateriaPrimera> materiaPrimera = materiaPrimeraService.findById(id);

        if (materiaPrimera.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        materiaPrimeraService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
