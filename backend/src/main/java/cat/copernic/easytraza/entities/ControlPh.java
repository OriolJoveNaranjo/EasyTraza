/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

    public ControlPh() {
    }

    public Long getId() {
        return id;
    }

    public Double getValorPh() {
        return valorPh;
    }

    public LocalDateTime getDataControl() {
        return dataControl;
    }

    public String getObservacions() {
        return observacions;
    }

    public Usuari getUsuari() {
        return usuari;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setValorPh(Double valorPh) {
        this.valorPh = valorPh;
    }

    public void setDataControl(LocalDateTime dataControl) {
        this.dataControl = dataControl;
    }

    public void setObservacions(String observacions) {
        this.observacions = observacions;
    }

    public void setUsuari(Usuari usuari) {
        this.usuari = usuari;
    }
}
