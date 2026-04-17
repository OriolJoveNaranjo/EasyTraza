/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cat.copernic.easytraza.repository;

import cat.copernic.easytraza.entities.ProducteFinal;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author orjon
 * Repository per gestionar productes finals
 */
public interface ProducteFinalRepository extends JpaRepository<ProducteFinal, Long> {

    boolean existsByNom(String nom);
}
