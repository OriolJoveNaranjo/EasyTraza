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
    @ManyToOne
    private Usuari usuariObertura;
    /**
     * Executa l'operació LotProveidor.
     */

    public LotProveidor() {
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

    public String getIdentificadorLot() {
        return identificadorLot;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     */

    public void setIdentificadorLot(String identificadorLot) {
        this.identificadorLot = identificadorLot;
    }
    /**
     * Retorna el valor de la propietat indicada.
     */

    public Proveidor getProveidor() {
        return proveidor;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     */

    public void setProveidor(Proveidor proveidor) {
        this.proveidor = proveidor;
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

    public LocalDate getDataCaducitat() {
        return dataCaducitat;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     */

    public void setDataCaducitat(LocalDate dataCaducitat) {
        this.dataCaducitat = dataCaducitat;
    }
    /**
     * Retorna el valor de la propietat indicada.
     */

    public LocalDateTime getDataObertura() {
        return dataObertura;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     */

    public void setDataObertura(LocalDateTime dataObertura) {
        this.dataObertura = dataObertura;
    }
    /**
     * Retorna el valor de la propietat indicada.
     */

    public LocalDateTime getDataAcabament() {
        return dataAcabament;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     */

    public void setDataAcabament(LocalDateTime dataAcabament) {
        this.dataAcabament = dataAcabament;
    }
    /**
     * Retorna el valor de la propietat indicada.
     */

    public EstatLot getEstat() {
        return estat;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     */

    public void setEstat(EstatLot estat) {
        this.estat = estat;
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

    public Usuari getUsuariObertura() {
        return usuariObertura;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     */

    public void setUsuariObertura(Usuari usuariObertura) {
        this.usuariObertura = usuariObertura;
    }
    
}
