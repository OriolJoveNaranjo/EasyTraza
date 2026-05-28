package cat.copernic.easytraza.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 *
 * @author orjon
 */
@Entity
public class ControlPh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double valorPh;

    private LocalDateTime dataControl;

    @Column(length = 500)
    private String observacions;

    @ManyToOne
    private Usuari usuari;
    /**
     * Executa l'operació ControlPh.
     */

    public ControlPh() {
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public Long getId() {
        return id;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public Double getValorPh() {
        return valorPh;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public LocalDateTime getDataControl() {
        return dataControl;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public String getObservacions() {
        return observacions;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public Usuari getUsuari() {
        return usuari;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param id
     */

    public void setId(Long id) {
        this.id = id;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param valorPh
     */

    public void setValorPh(Double valorPh) {
        this.valorPh = valorPh;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param dataControl
     */

    public void setDataControl(LocalDateTime dataControl) {
        this.dataControl = dataControl;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param observacions
     */

    public void setObservacions(String observacions) {
        this.observacions = observacions;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param usuari
     */

    public void setUsuari(Usuari usuari) {
        this.usuari = usuari;
    }
}
