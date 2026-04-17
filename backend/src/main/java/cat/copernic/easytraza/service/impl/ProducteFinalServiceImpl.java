/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.service.impl;

import cat.copernic.easytraza.entities.ProducteFinal;
import cat.copernic.easytraza.repository.ProducteFinalRepository;
import cat.copernic.easytraza.service.ProducteFinalService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author orjon
 */
@Service
public class ProducteFinalServiceImpl implements ProducteFinalService {

    private final ProducteFinalRepository producteFinalRepository;

    public ProducteFinalServiceImpl(ProducteFinalRepository producteFinalRepository) {
        this.producteFinalRepository = producteFinalRepository;
    }

    @Override
    public List<ProducteFinal> findAll() {
        return producteFinalRepository.findAll();
    }

    @Override
    public ProducteFinal save(ProducteFinal producteFinal) {
        if (producteFinalRepository.existsByNom(producteFinal.getNom())) {
            throw new RuntimeException("Ja existeix un producte final amb aquest nom");
        }

        return producteFinalRepository.save(producteFinal);
    }

}
