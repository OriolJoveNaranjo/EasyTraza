/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

    public LiniaAlbaraProveidor() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AlbaraProveidor getAlbaraProveidor() {
        return albaraProveidor;
    }

    public void setAlbaraProveidor(AlbaraProveidor albaraProveidor) {
        this.albaraProveidor = albaraProveidor;
    }

    public MateriaPrimera getMateriaPrimera() {
        return materiaPrimera;
    }

    public void setMateriaPrimera(MateriaPrimera materiaPrimera) {
        this.materiaPrimera = materiaPrimera;
    }

    public Double getQuantitat() {
        return quantitat;
    }

    public void setQuantitat(Double quantitat) {
        this.quantitat = quantitat;
    }

    public String getUnitat() {
        return unitat;
    }

    public void setUnitat(String unitat) {
        this.unitat = unitat;
    }

    public LotProveidor getLot() {
        return lot;
    }

    public void setLot(LotProveidor lot) {
        this.lot = lot;
    }
}
    
