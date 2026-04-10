/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.service.impl;

import cat.copernic.easytraza.entities.MateriaPrimera;
import cat.copernic.easytraza.repository.MateriaPrimeraRepository;
import cat.copernic.easytraza.service.MateriaPrimeraService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author orjon
 */
@Service
public class MateriaPrimeraServiceImpl implements MateriaPrimeraService {

    private final MateriaPrimeraRepository materiaPrimeraRepository;

    public MateriaPrimeraServiceImpl(MateriaPrimeraRepository materiaPrimeraRepository) {
        this.materiaPrimeraRepository = materiaPrimeraRepository;
    }

    @Override
    public List<MateriaPrimera> findAll() {
        return materiaPrimeraRepository.findAll();
    }

    @Override
    public Optional<MateriaPrimera> findById(Long id) {
        return materiaPrimeraRepository.findById(id);
    }

    @Override
    public MateriaPrimera save(MateriaPrimera materiaPrimera) {
        return materiaPrimeraRepository.save(materiaPrimera);
    }

    @Override
    public MateriaPrimera update(Long id, MateriaPrimera materiaPrimera) {
        Optional<MateriaPrimera> existent = materiaPrimeraRepository.findById(id);

        if (existent.isPresent()) {
            MateriaPrimera actual = existent.get();
            actual.setNom(materiaPrimera.getNom());
            actual.setDescripcio(materiaPrimera.getDescripcio());
            return materiaPrimeraRepository.save(actual);
        }

        return null;
    }

    @Override
    public void deleteById(Long id) {
        materiaPrimeraRepository.deleteById(id);
    }
}
