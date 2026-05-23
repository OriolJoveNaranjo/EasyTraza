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

    private LocalDateTime dataRegistre;
    /**
     * Executa l'operació Tracabilitat.
     */

    public Tracabilitat() {
    }
    /**
     * Retorna el valor de la propietat indicada.
     */

    public Long getId() {
        return id;
    }
    /**
     * Retorna el valor de la propietat indicada.
     */

    public LotProveidor getLotProveidor() {
        return lotProveidor;
    }
    /**
     * Retorna el valor de la propietat indicada.
     */

    public ProducteFinal getProducteFinal() {
        return producteFinal;
    }
    /**
     * Retorna el valor de la propietat indicada.
     */

    public LiniaAlbaraClient getLiniaAlbaraClient() {
        return liniaAlbaraClient;
    }
    /**
     * Retorna el valor de la propietat indicada.
     */

    public LocalDateTime getDataRegistre() {
        return dataRegistre;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     */

    public void setId(Long id) {
        this.id = id;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     */

    public void setLotProveidor(LotProveidor lotProveidor) {
        this.lotProveidor = lotProveidor;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     */

    public void setProducteFinal(ProducteFinal producteFinal) {
        this.producteFinal = producteFinal;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     */

    public void setLiniaAlbaraClient(LiniaAlbaraClient liniaAlbaraClient) {
        this.liniaAlbaraClient = liniaAlbaraClient;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     */

    public void setDataRegistre(LocalDateTime dataRegistre) {
        this.dataRegistre = dataRegistre;
    }
}
