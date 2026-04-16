/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.controller.api;

import cat.copernic.easytraza.entities.Usuari;
import cat.copernic.easytraza.service.UsuariService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author orjon Controlador REST per gestionar usuaris.
 */
@RestController
@RequestMapping("/api/usuaris")
public class UsuariRestController {

    private final UsuariService usuariService;

    public UsuariRestController(UsuariService usuariService) {
        this.usuariService = usuariService;
    }

    @GetMapping
    public ResponseEntity<List<Usuari>> getAll() {
        return ResponseEntity.ok(usuariService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuari> getById(@PathVariable Long id) {
        Optional<Usuari> usuari = usuariService.findById(id);
        return usuari.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Usuari usuari) {
        try {
            Usuari creat = usuariService.save(usuari);
            return ResponseEntity.ok(creat);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Usuari usuari) {
        try {
            Usuari actualitzat = usuariService.update(id, usuari);
            return ResponseEntity.ok(actualitzat);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Usuari> usuari = usuariService.findById(id);

        if (usuari.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        usuariService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
