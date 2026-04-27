/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.repository;

import cat.copernic.easytraza.entities.Proveidor;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author orjon
 *
 * Repositori per gestionar proveïdors.
 *
 */
public interface ProveidorRepository extends JpaRepository<Proveidor, Long> {

    boolean existsByCif(String cif);

    boolean existsByCifAndIdNot(String cif, Long id);
    Optional<Proveidor> findByCif (String cif);
}
