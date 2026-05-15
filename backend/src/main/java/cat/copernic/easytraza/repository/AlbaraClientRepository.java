/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.repository;

import cat.copernic.easytraza.entities.AlbaraClient;
import cat.copernic.easytraza.enums.EstatAlbaraClient;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author orjon
 */
public interface AlbaraClientRepository extends JpaRepository<AlbaraClient, Long> {

    List<AlbaraClient> findByClientId(Long clientId);

    List<AlbaraClient> findByEstat(EstatAlbaraClient estat);

    List<AlbaraClient> findByClientIdAndEstat(Long clientId, EstatAlbaraClient estat);

    List<AlbaraClient> findAllByOrderByDataAsc();

    List<AlbaraClient> findAllByOrderByDataDesc();

    boolean existsByClientIdAndEstat(Long clientId, EstatAlbaraClient estat);

    boolean existsByClientId(Long clientId);

    
}
