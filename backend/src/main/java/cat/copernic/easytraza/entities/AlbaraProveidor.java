package cat.copernic.easytraza.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author orjon
 */
@Entity
public class AlbaraProveidor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate dataRecepcio;

    @ManyToOne(optional = false)
    private Proveidor proveidor;

    @OneToMany(mappedBy = "albaraProveidor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LiniaAlbaraProveidor> linies = new ArrayList<>();

    @Column(unique = true)
    private String numeroAlbara;
    
    @OneToMany(mappedBy = "albaraProveidor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FitxerAlbaraProveidor> fitxers = new ArrayList<>();
    @ManyToOne
    private Usuari usuariAlta;
    /**
     * Executa l'operació AlbaraProveidor.
     */

    public AlbaraProveidor() {
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public Long getId() {
        return id;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param id
     */

    public void setId(Long id) {
        this.id = id;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public LocalDate getDataRecepcio() {
        return dataRecepcio;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param dataRecepcio
     */

    public void setDataRecepcio(LocalDate dataRecepcio) {
        this.dataRecepcio = dataRecepcio;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public Proveidor getProveidor() {
        return proveidor;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param proveidor
     */

    public void setProveidor(Proveidor proveidor) {
        this.proveidor = proveidor;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public List<LiniaAlbaraProveidor> getLinies() {
        return linies;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param linies
     */

    public void setLinies(List<LiniaAlbaraProveidor> linies) {
        this.linies = linies;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public String getNumeroAlbara() {
        return numeroAlbara;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param numeroAlbara
     */

    public void setNumeroAlbara(String numeroAlbara) {
        this.numeroAlbara = numeroAlbara;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public List<FitxerAlbaraProveidor> getFitxers() {
        return fitxers;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param fitxers
     */

    public void setFitxers(List<FitxerAlbaraProveidor> fitxers) {
        this.fitxers = fitxers;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public Usuari getUsuariAlta() {
        return usuariAlta;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param usuariAlta
     */

    public void setUsuariAlta(Usuari usuariAlta) {
        this.usuariAlta = usuariAlta;
    }
    

}
