/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getDataExpiracio() {
        return dataExpiracio;
    }

    public boolean isUtilitzat() {
        return utilitzat;
    }

    public Usuari getUsuari() {
        return usuari;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setDataExpiracio(LocalDateTime dataExpiracio) {
        this.dataExpiracio = dataExpiracio;
    }

    public void setUtilitzat(boolean utilitzat) {
        this.utilitzat = utilitzat;
    }

    public void setUsuari(Usuari usuari) {
        this.usuari = usuari;
    }
}
