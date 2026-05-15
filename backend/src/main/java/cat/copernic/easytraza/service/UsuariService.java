/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.service;

import cat.copernic.easytraza.entities.Usuari;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author orjon
 *
 * Servei de gestió d'usuaris.
 */
public interface UsuariService {

    List<Usuari> findAll();

    Optional<Usuari> findById(Long id);

    Usuari save(Usuari usuari);

    Usuari update(Long id, Usuari usuari);

    String deleteById(Long id);

    void activar(Long id);
}
