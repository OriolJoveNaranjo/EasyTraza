/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.repository;

import cat.copernic.easytraza.entities.Client;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author orjon
 */
public interface ClientRepository extends JpaRepository<Client, Long> {

    boolean existsByNif(String nif);

    boolean existsByNifAndIdNot(String nif, Long id);

    List<Client> findByNomContainingIgnoreCase(String nom);

    List<Client> findByNifContainingIgnoreCase(String nif);

    List<Client> findByNomContainingIgnoreCaseOrNifContainingIgnoreCase(String nom, String nif);

    List<Client> findAllByOrderByNomAsc();

    List<Client> findAllByOrderByNomDesc();

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);
}
