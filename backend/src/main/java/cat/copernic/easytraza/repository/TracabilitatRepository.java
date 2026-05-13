/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.repository;

import cat.copernic.easytraza.entities.Tracabilitat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author orjon
 */
public interface TracabilitatRepository extends JpaRepository<Tracabilitat, Long> {

    List<Tracabilitat> findByLotProveidorId(Long lotId);

}
