/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.entities;

import cat.copernic.easytraza.enums.EstatLot;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author orjon
 */
@Entity
public class LotProveidor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String identificadorLot;

    @ManyToOne(optional = false)
    private Proveidor proveidor;

    @ManyToOne(optional = false)
    private MateriaPrimera materiaPrimera;

    @Column(nullable = false)
    private Double quantitat;

    private String unitat;

    private LocalDate dataCaducitat;

    private LocalDateTime dataObertura;

    private LocalDateTime dataAcabament;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstatLot estat = EstatLot.EN_ESTOC;

    @ManyToOne(optional = false)
    private AlbaraProveidor albaraProveidor;

    public LotProveidor() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentificadorLot() {
        return identificadorLot;
    }

    public void setIdentificadorLot(String identificadorLot) {
        this.identificadorLot = identificadorLot;
    }

    public Proveidor getProveidor() {
        return proveidor;
    }

    public void setProveidor(Proveidor proveidor) {
        this.proveidor = proveidor;
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

    public LocalDate getDataCaducitat() {
        return dataCaducitat;
    }

    public void setDataCaducitat(LocalDate dataCaducitat) {
        this.dataCaducitat = dataCaducitat;
    }

    public LocalDateTime getDataObertura() {
        return dataObertura;
    }

    public void setDataObertura(LocalDateTime dataObertura) {
        this.dataObertura = dataObertura;
    }

    public LocalDateTime getDataAcabament() {
        return dataAcabament;
    }

    public void setDataAcabament(LocalDateTime dataAcabament) {
        this.dataAcabament = dataAcabament;
    }

    public EstatLot getEstat() {
        return estat;
    }

    public void setEstat(EstatLot estat) {
        this.estat = estat;
    }

    public AlbaraProveidor getAlbaraProveidor() {
        return albaraProveidor;
    }

    public void setAlbaraProveidor(AlbaraProveidor albaraProveidor) {
        this.albaraProveidor = albaraProveidor;
    }
}
    
