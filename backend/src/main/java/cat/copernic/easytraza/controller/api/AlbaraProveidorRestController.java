/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.controller.api;


import cat.copernic.easytraza.entities.AlbaraProveidor;
import cat.copernic.easytraza.service.AlbaraProveidorService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author orjon
 */
@RestController
@RequestMapping("/api/albarans-proveidor")
public class AlbaraProveidorRestController {

    private final AlbaraProveidorService albaraService;

    public AlbaraProveidorRestController(AlbaraProveidorService albaraService) {
        this.albaraService = albaraService;
    }

    @GetMapping
    public ResponseEntity<List<AlbaraProveidor>> getAll() {
        return ResponseEntity.ok(albaraService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbaraProveidor> getById(@PathVariable Long id) {
        Optional<AlbaraProveidor> albara = albaraService.findById(id);
        return albara.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AlbaraProveidor albaraProveidor) {
        try {
            return ResponseEntity.ok(albaraService.save(albaraProveidor));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody AlbaraProveidor albaraProveidor) {
        try {
            return ResponseEntity.ok(albaraService.update(id, albaraProveidor));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (albaraService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        albaraService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
