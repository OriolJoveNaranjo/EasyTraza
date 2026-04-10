/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.api;

import cat.copernic.easytraza.entities.MateriaPrimera;
import cat.copernic.easytraza.service.MateriaPrimeraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author orjon
 * Controlador REST per gestionar matèries primeres
 */
@RestController
@RequestMapping("/api/materies-primeres")
public class MateriaPrimeraControllerApi {

    private final MateriaPrimeraService materiaPrimeraService;

    public MateriaPrimeraControllerApi(MateriaPrimeraService materiaPrimeraService) {
        this.materiaPrimeraService = materiaPrimeraService;
    }

    @GetMapping
    public List<MateriaPrimera> getAll() {
        return materiaPrimeraService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MateriaPrimera> getById(@PathVariable Long id) {
        Optional<MateriaPrimera> materiaPrimera = materiaPrimeraService.findById(id);
        return materiaPrimera.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MateriaPrimera> create(@RequestBody MateriaPrimera materiaPrimera) {
        MateriaPrimera creada = materiaPrimeraService.save(materiaPrimera);
        return ResponseEntity.ok(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MateriaPrimera> update(@PathVariable Long id, @RequestBody MateriaPrimera materiaPrimera) {
        MateriaPrimera actualitzada = materiaPrimeraService.update(id, materiaPrimera);

        if (actualitzada == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(actualitzada);
    }

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
