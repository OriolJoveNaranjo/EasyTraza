/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.repository;

import cat.copernic.easytraza.entities.MateriaPrimera;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author orjon
 * Repositori per gestionar matèries primeres.
 */

public interface MateriaPrimeraRepository extends JpaRepository<MateriaPrimera, Long> {
}
