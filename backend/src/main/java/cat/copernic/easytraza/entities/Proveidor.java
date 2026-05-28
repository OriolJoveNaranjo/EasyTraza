package cat.copernic.easytraza.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

/**
 *
 * @author orjon
 *
 * Entitat que representa un proveïdor.
 */
@Entity
public class Proveidor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(
            regexp = "^[A-HJNP-SUVW][0-9]{7}[0-9A-J]$|^[0-9]{8}[A-Z]$|^[XYZ][0-9]{7}[A-Z]$",
            message = "El CIF/NIF/NIE no té un format vàlid"
    )
    @Column(nullable = false, unique = true)
    private String cif;

    @Column(nullable = false)
    private String nom;

    private String telefon;

    
    @Email(message = "El format del correu electrònic no és vàlid")
    private String email;

    private String adreca;

    @Column(length = 500)
    private String observacions;

    private boolean actiu = true;
    /**
     * Executa l'operació Proveidor.
     */

    public Proveidor() {
    }
    /**
     * Executa l'operació Proveidor.
     * @param cif
     * @param nom
     * @param adreca
     * @param observacions
     * @param email
     * @param telefon
     */

    public Proveidor(String cif, String nom, String adreca, String observacions, String email, String telefon) {
        this.cif = cif;
        this.nom = nom;
        this.adreca = adreca;
        this.observacions = observacions;
        this.email = email;
        this.telefon = telefon;
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

    public String getCif() {
        return cif;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param cif
     */

    public void setCif(String cif) {
        this.cif = cif;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public String getNom() {
        return nom;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param nom
     */

    public void setNom(String nom) {
        this.nom = nom;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public String getAdreca() {
        return adreca;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param adreca
     */

    public void setAdreca(String adreca) {
        this.adreca = adreca;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public String getTelefon() {
        return telefon;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param telefon
     */

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public String getEmail() {
        return email;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param email
     */

    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public String getObservacions() {
        return observacions;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param observacions
     */

    public void setObservacions(String observacions) {
        this.observacions = observacions;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public boolean isActiu() {
        return actiu;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param actiu
     */

    public void setActiu(boolean actiu) {
        this.actiu = actiu;
    }

}
