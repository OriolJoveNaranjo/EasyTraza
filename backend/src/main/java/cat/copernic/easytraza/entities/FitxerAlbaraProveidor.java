/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

/**
 *
 * @author orjon
 */
@Entity
public class FitxerAlbaraProveidor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomFitxer;

    private String tipusFitxer;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] dades;

    @ManyToOne
    @JoinColumn(name = "albara_proveidor_id")
    private AlbaraProveidor albaraProveidor;
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

    public String getNomFitxer() {
        return nomFitxer;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     */

    public void setNomFitxer(String nomFitxer) {
        this.nomFitxer = nomFitxer;
    }
    /**
     * Retorna el valor de la propietat indicada.
     */

    public String getTipusFitxer() {
        return tipusFitxer;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     */

    public void setTipusFitxer(String tipusFitxer) {
        this.tipusFitxer = tipusFitxer;
    }
    /**
     * Retorna el valor de la propietat indicada.
     */

    public byte[] getDades() {
        return dades;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     */

    public void setDades(byte[] dades) {
        this.dades = dades;
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
}
