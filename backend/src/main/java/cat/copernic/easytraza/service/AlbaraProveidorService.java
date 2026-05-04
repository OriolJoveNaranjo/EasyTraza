/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.service;

import cat.copernic.easytraza.entities.AlbaraProveidor;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author orjon
 */
public interface AlbaraProveidorService {

    List<AlbaraProveidor> findAll();

    Optional<AlbaraProveidor> findById(Long id);

    AlbaraProveidor save(AlbaraProveidor albaraProveidor);

    AlbaraProveidor update(Long id, AlbaraProveidor albaraProveidor);

    void deleteById(Long id);

    List<AlbaraProveidor> filtrar(Long proveidorId, String ordre);
}
