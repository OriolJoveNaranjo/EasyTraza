/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.service;

import cat.copernic.easytraza.entities.Proveidor;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author orjon
 *
 * Servei de gestió de proveïdors.
 *
 */
public interface ProveidorService {

    List<Proveidor> findAll();

    Optional<Proveidor> findById(Long id);

    Proveidor save(Proveidor proveidor);

    Proveidor update(Long id, Proveidor proveidor);

    void deleteById(Long id);

    List<Proveidor> filtrar(String filtre, String ordre);
}
