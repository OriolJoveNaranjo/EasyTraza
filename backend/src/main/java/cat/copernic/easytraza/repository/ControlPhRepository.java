/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.repository;

import cat.copernic.easytraza.entities.ControlPh;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author orjon
 */
public interface ControlPhRepository extends JpaRepository<ControlPh, Long> {

    List<ControlPh> findAllByOrderByDataControlDesc();

    boolean existsByUsuariId(Long usuariId);

    boolean existsById(Long id);
}
