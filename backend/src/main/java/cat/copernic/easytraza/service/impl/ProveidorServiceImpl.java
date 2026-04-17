/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.service.impl;

import cat.copernic.easytraza.entities.Proveidor;
import cat.copernic.easytraza.repository.ProveidorRepository;
import cat.copernic.easytraza.service.ProveidorService;
import cat.copernic.easytraza.validation.CifValidator;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 *
 *
 * @author orjon
 *
 * Implementació del servei de proveïdors.
 */
@Service
public class ProveidorServiceImpl implements ProveidorService {

    private final ProveidorRepository proveidorRepository;

    public ProveidorServiceImpl(ProveidorRepository proveidorRepository) {
        this.proveidorRepository = proveidorRepository;
    }

    @Override
    public List<Proveidor> findAll() {
        return proveidorRepository.findAll();
    }

    @Override
    public Optional<Proveidor> findById(Long id) {
        return proveidorRepository.findById(id);
    }

    @Override
    public Proveidor save(Proveidor proveidor) {
        if (proveidorRepository.existsByCif(proveidor.getCif())) {
            throw new RuntimeException("Ja existeix un proveïdor amb aquest CIF");
        }
        if (!CifValidator.validarCIF(proveidor.getCif())) {
            throw new IllegalArgumentException("El CIF no és vàlid");
        }

        return proveidorRepository.save(proveidor);
    }

    @Override
    public Proveidor update(Long id, Proveidor proveidor) {
        Optional<Proveidor> existent = proveidorRepository.findById(id);

        if (existent.isEmpty()) {
            throw new RuntimeException("El proveïdor no existeix");
        }

        if (proveidorRepository.existsByCifAndIdNot(proveidor.getCif(), id)) {
            throw new RuntimeException("Ja existeix un proveïdor amb aquest CIF");
        }
        if (!CifValidator.validarCIF(proveidor.getCif())) {
            throw new IllegalArgumentException("El CIF no és vàlid");
        }

        Proveidor actual = existent.get();
        actual.setNom(proveidor.getNom());
        actual.setCif(proveidor.getCif());
        actual.setTelefon(proveidor.getTelefon());
        actual.setEmail(proveidor.getEmail());
        actual.setAdreca(proveidor.getAdreca());
        actual.setObservacions(proveidor.getObservacions());

        return proveidorRepository.save(actual);
    }

    @Override
    public void deleteById(Long id) {
        proveidorRepository.deleteById(id);
    }
}
