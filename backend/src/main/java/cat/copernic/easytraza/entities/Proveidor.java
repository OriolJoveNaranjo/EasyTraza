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
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

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

    private String email;

    private String adreca;

    @Column(length = 500)
    private String observacions;

    public Proveidor() {
    }

    public Proveidor(String cif, String nom, String adreca, String observacions, String email, String telefon) {
        this.cif = cif;
        this.nom = nom;
        this.adreca = adreca;
        this.observacions = observacions;
        this.email = email;
        this.telefon = telefon;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdreca() {
        return adreca;
    }

    public void setAdreca(String adreca) {
        this.adreca = adreca;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getObservacions() {
        return observacions;
    }

    public void setObservacions(String observacions) {
        this.observacions = observacions;
    }

}
