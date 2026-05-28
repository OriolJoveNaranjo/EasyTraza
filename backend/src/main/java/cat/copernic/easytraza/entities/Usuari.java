package cat.copernic.easytraza.entities;

import cat.copernic.easytraza.enums.RolUsuari;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 *
 * @author orjon
 *
 * Entitat que representa un usuari.
 */
@Entity
public class Usuari {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "El correu electrònic és obligatori")
    @Email(message = "El format del correu electrònic no és vàlid")
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolUsuari rol;

    @Column(nullable = false)
    private boolean actiu;

    private String foto;
    /**
     * Executa l'operació Usuari.
     */

    public Usuari() {
    }
    /**
     * Executa l'operació Usuari.
     * @param nom
     * @param email
     * @param password
     * @param rol
     * @param actiu
     */

    public Usuari(String nom, String email, String password, RolUsuari rol, boolean actiu) {
        this.nom = nom;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.actiu = actiu;
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

    public String getPassword() {
        return password;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param password
     */

    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public RolUsuari getRol() {
        return rol;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param rol
     */

    public void setRol(RolUsuari rol) {
        this.rol = rol;
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
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public String getFoto() {
        return foto;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param foto
     */

    public void setFoto(String foto) {
        this.foto = foto;
    }

}
