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
public class MateriaPrimera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String nom;
    
    @Size(max = 255)
    @Column(length = 255)
    private String descripcio;
    
    private boolean actiu = true;
    /**
     * Executa l'operació MateriaPrimera.
     */

    public MateriaPrimera() {
    }
    /**
     * Executa l'operació MateriaPrimera.
     * @param nom
     * @param descripcio
     */

    public MateriaPrimera(String nom, String descripcio) {
        this.nom = nom;
        this.descripcio = descripcio;
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

    public String getDescripcio() {
        return descripcio;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param descripcio
     */

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
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
