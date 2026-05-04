/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

/**
 *
 * @author orjon
 */
@Entity
public class LiniaAlbaraClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private AlbaraClient albaraClient;

    @ManyToOne
    private ProducteFinal producte;

    private Double quantitat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AlbaraClient getAlbaraClient() {
        return albaraClient;
    }

    public void setAlbaraClient(AlbaraClient albaraClient) {
        this.albaraClient = albaraClient;
    }

    public ProducteFinal getProducte() {
        return producte;
    }

    public void setProducte(ProducteFinal producte) {
        this.producte = producte;
    }

    public Double getQuantitat() {
        return quantitat;
    }

    public void setQuantitat(Double quantitat) {
        this.quantitat = quantitat;
    }
    
}
