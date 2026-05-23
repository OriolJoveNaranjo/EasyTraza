/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.controller.api;

import cat.copernic.easytraza.entities.ProducteFinal;
import cat.copernic.easytraza.service.ProducteFinalService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author orjon
 * Controlador REST per gestionar productes finals.
 */
@RestController
@RequestMapping("/api/productes-finals")
public class ProducteFinalRestController {

    private final ProducteFinalService producteFinalService;
    /**
     * Executa l'operació ProducteFinalRestController.
     */

    public ProducteFinalRestController(ProducteFinalService producteFinalService) {
        this.producteFinalService = producteFinalService;
    }
    /**
     * Retorna el valor de la propietat indicada.
     */

    @GetMapping
    public ResponseEntity<List<ProducteFinal>> getAll() {
        return ResponseEntity.ok(producteFinalService.findAll());
    }
    /**
     * Retorna el valor de la propietat indicada.
     */

    @GetMapping("/{id}")
    public ResponseEntity<ProducteFinal> getById(@PathVariable Long id) {
        Optional<ProducteFinal> producteFinal = producteFinalService.findById(id);
        return producteFinal.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    /**
     * Valida i desa la informació rebuda.
     */

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProducteFinal producteFinal) {
        try {
            ProducteFinal creat = producteFinalService.save(producteFinal);
            return ResponseEntity.ok(creat);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    /**
     * Actualitza una entitat existent amb les dades indicades.
     */

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProducteFinal producteFinal) {
        try {
            ProducteFinal actualitzat = producteFinalService.update(id, producteFinal);
            return ResponseEntity.ok(actualitzat);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    /**
     * Elimina o desactiva el registre indicat segons les regles de negoci.
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<ProducteFinal> producteFinal = producteFinalService.findById(id);

        if (producteFinal.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        producteFinalService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
