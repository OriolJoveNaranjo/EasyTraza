/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.service;

import cat.copernic.easytraza.entities.MateriaPrimera;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author orjon Servei de gestió de matèries primeres.
 */
public interface MateriaPrimeraService {

    List<MateriaPrimera> findAll();

    Optional<MateriaPrimera> findById(Long id);

    MateriaPrimera save(MateriaPrimera materiaPrimera);

    MateriaPrimera update(Long id, MateriaPrimera materiaPrimera);

    String deleteById(Long id);

    void activar(Long id);

}
