package cat.copernic.easytraza.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;

/**
 *
 * @author orjon Token temporal para recuperar la contraseña de un usuario.
 */
@Entity
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private LocalDateTime dataExpiracio;

    private boolean utilitzat;

    @OneToOne
    @JoinColumn(name = "usuari_id", nullable = false)
    private Usuari usuari;
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

    public String getToken() {
        return token;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public LocalDateTime getDataExpiracio() {
        return dataExpiracio;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public boolean isUtilitzat() {
        return utilitzat;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public Usuari getUsuari() {
        return usuari;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param id
     */

    public void setId(Long id) {
        this.id = id;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param token
     */

    public void setToken(String token) {
        this.token = token;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param dataExpiracio
     */

    public void setDataExpiracio(LocalDateTime dataExpiracio) {
        this.dataExpiracio = dataExpiracio;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param utilitzat
     */

    public void setUtilitzat(boolean utilitzat) {
        this.utilitzat = utilitzat;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param usuari
     */

    public void setUsuari(Usuari usuari) {
        this.usuari = usuari;
    }
}
