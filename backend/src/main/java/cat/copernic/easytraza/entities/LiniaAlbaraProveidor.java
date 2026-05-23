package cat.copernic.easytraza.entities;

import jakarta.persistence.*;

/**
 *
 * @author orjon
 */
@Entity
public class LiniaAlbaraProveidor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private AlbaraProveidor albaraProveidor;

    @ManyToOne(optional = false)
    private MateriaPrimera materiaPrimera;

    @Column(nullable = false)
    private Double quantitat;

    private String unitat;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    private LotProveidor lot;
    /**
     * Executa l'operació LiniaAlbaraProveidor.
     */

    public LiniaAlbaraProveidor() {
    }
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

    public AlbaraProveidor getAlbaraProveidor() {
        return albaraProveidor;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     */

    public void setAlbaraProveidor(AlbaraProveidor albaraProveidor) {
        this.albaraProveidor = albaraProveidor;
    }
    /**
     * Retorna el valor de la propietat indicada.
     */

    public MateriaPrimera getMateriaPrimera() {
        return materiaPrimera;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     */

    public void setMateriaPrimera(MateriaPrimera materiaPrimera) {
        this.materiaPrimera = materiaPrimera;
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
    /**
     * Retorna el valor de la propietat indicada.
     */

    public String getUnitat() {
        return unitat;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     */

    public void setUnitat(String unitat) {
        this.unitat = unitat;
    }
    /**
     * Retorna el valor de la propietat indicada.
     */

    public LotProveidor getLot() {
        return lot;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     */

    public void setLot(LotProveidor lot) {
        this.lot = lot;
    }
}
    
