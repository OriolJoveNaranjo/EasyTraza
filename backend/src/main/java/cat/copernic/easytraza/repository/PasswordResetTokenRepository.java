/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.repository;

import cat.copernic.easytraza.entities.PasswordResetToken;
import cat.copernic.easytraza.entities.Usuari;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author orjon
 */
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    void deleteByUsuari(Usuari usuari);

    void deleteByUsuariId(Long usuariId);
}
