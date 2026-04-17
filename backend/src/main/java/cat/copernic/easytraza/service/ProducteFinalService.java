/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cat.copernic.easytraza.service;

import cat.copernic.easytraza.entities.ProducteFinal;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author orjon
 */
public interface ProducteFinalService {

    List<ProducteFinal> findAll();

    Optional<ProducteFinal> findById(Long id);

    ProducteFinal save(ProducteFinal producteFinal);

    ProducteFinal update(Long id, ProducteFinal producteFinal);

    void deleteById(Long id);
}
