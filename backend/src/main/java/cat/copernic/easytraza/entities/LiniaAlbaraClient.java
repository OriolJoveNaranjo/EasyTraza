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
    /**
     * Retorna el valor de la propietat indicada.
     */

    
    

    public Long getId() {
        return id;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     */

    public void setId(Long id) {
        this.id = id;
    }
    /**
     * Retorna el valor de la propietat indicada.
     */

    public AlbaraClient getAlbaraClient() {
        return albaraClient;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     */

    public void setAlbaraClient(AlbaraClient albaraClient) {
        this.albaraClient = albaraClient;
    }
    /**
     * Retorna el valor de la propietat indicada.
     */

    public ProducteFinal getProducte() {
        return producte;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     */

    public void setProducte(ProducteFinal producte) {
        this.producte = producte;
    }
    /**
     * Retorna el valor de la propietat indicada.
     */

    public Double getQuantitat() {
        return quantitat;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     */

    public void setQuantitat(Double quantitat) {
        this.quantitat = quantitat;
    }

    

}
