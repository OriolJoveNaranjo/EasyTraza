/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.repository;

import cat.copernic.easytraza.entities.LotProveidor;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author orjon
 */
public interface LotProveidorRepository extends JpaRepository<LotProveidor, Long> {

    boolean existsByIdentificadorLotAndProveidorId(String identificadorLot, Long proveidorId);

    Optional<LotProveidor> findByIdentificadorLotAndProveidorId(String identificadorLot, Long proveidorId);
}
