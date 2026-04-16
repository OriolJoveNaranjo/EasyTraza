/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.repository;

import cat.copernic.easytraza.entities.Usuari;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author orjon
 */
public interface UsuariRepository extends JpaRepository<Usuari, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);
}
