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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 *
 * @author orjon
 */
@Entity
public class ProducteFinal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String nom;
    
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String descripcio;
    
    private boolean actiu = true;
    /**
     * Executa l'operació ProducteFinal.
     */

    public ProducteFinal() {

    }
    /**
     * Executa l'operació ProducteFinal.
     */

    public ProducteFinal(String nom, String descripcio) {

        this.nom = nom;
        this.descripcio = descripcio;
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

    public String getNom() {
        return nom;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     */

    public void setNom(String nom) {
        this.nom = nom;
    }
    /**
     * Retorna el valor de la propietat indicada.
     */

    public String getDescripcio() {
        return descripcio;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     */

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }
    /**
     * Retorna el valor de la propietat indicada.
     */

    public boolean isActiu() {
        return actiu;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     */

    public void setActiu(boolean actiu) {
        this.actiu = actiu;
    }
    

}
