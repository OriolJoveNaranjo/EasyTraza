/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.entities;

import java.time.LocalDateTime;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

/**
 *
 * @author orjon
 */

@Entity
public class Tracabilitat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private LotProveidor lotProveidor;

    @ManyToOne(optional = false)
    private ProducteFinal producteFinal;

    @ManyToOne(optional = false)
    private LiniaAlbaraClient liniaAlbaraClient;

    private Double quantitatUtilitzada;

    private LocalDateTime dataRegistre;

    public Tracabilitat() {
    }

    public Long getId() {
        return id;
    }

    public LotProveidor getLotProveidor() {
        return lotProveidor;
    }

    public ProducteFinal getProducteFinal() {
        return producteFinal;
    }

    public LiniaAlbaraClient getLiniaAlbaraClient() {
        return liniaAlbaraClient;
    }

    public Double getQuantitatUtilitzada() {
        return quantitatUtilitzada;
    }

    public LocalDateTime getDataRegistre() {
        return dataRegistre;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLotProveidor(LotProveidor lotProveidor) {
        this.lotProveidor = lotProveidor;
    }

    public void setProducteFinal(ProducteFinal producteFinal) {
        this.producteFinal = producteFinal;
    }

    public void setLiniaAlbaraClient(LiniaAlbaraClient liniaAlbaraClient) {
        this.liniaAlbaraClient = liniaAlbaraClient;
    }

    public void setQuantitatUtilitzada(Double quantitatUtilitzada) {
        this.quantitatUtilitzada = quantitatUtilitzada;
    }

    public void setDataRegistre(LocalDateTime dataRegistre) {
        this.dataRegistre = dataRegistre;
    }
}
