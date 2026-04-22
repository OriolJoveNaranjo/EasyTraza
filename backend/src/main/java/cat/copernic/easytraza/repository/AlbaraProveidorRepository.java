/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.repository;

import cat.copernic.easytraza.entities.AlbaraProveidor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author orjon
 */
public interface AlbaraProveidorRepository extends JpaRepository<AlbaraProveidor, Long> {

    boolean existsByNumeroAlbara(String numeroAlbara);

    boolean existsByNumeroAlbaraAndIdNot(String numeroAlbara, Long id);
}
