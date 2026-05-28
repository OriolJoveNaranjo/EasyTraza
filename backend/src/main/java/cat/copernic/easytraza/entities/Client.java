package cat.copernic.easytraza.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

/**
 *
 * @author orjon
 */
@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nif;

    @Column(nullable = false)
    private String nom;

    private String cognoms;

    private String adreca;

    private String registreSanitari;

    private String telefon;

    @Column(unique = true)    
    @Email(message = "El format del correu electrònic no és vàlid")
    private String email;

    private boolean actiu = true;
    /**
     * Executa l'operació Client.
     */

    public Client() {
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

    public String getNif() {
        return nif;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param nif
     */

    public void setNif(String nif) {
        this.nif = nif;
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

    public String getCognoms() {
        return cognoms;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param cognoms
     */

    public void setCognoms(String cognoms) {
        this.cognoms = cognoms;
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

    public String getRegistreSanitari() {
        return registreSanitari;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param registreSanitari
     */

    public void setRegistreSanitari(String registreSanitari) {
        this.registreSanitari = registreSanitari;
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
