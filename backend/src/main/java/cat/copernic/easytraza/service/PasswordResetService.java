package cat.copernic.easytraza.service;

import cat.copernic.easytraza.entities.PasswordResetToken;
import cat.copernic.easytraza.entities.Usuari;
import cat.copernic.easytraza.repository.PasswordResetTokenRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author orjon Servicio para gestionar tokens de recuperación de contraseña.
 */
@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;

    public PasswordResetService(PasswordResetTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public String crearToken(Usuari usuari) {
        tokenRepository.deleteByUsuari(usuari);
        tokenRepository.flush();

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUsuari(usuari);
        resetToken.setUtilitzat(false);
        resetToken.setDataExpiracio(LocalDateTime.now().plusMinutes(15));

        tokenRepository.save(resetToken);

        return token;
    }

    public PasswordResetToken validarToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token invàlid"));

        if (resetToken.isUtilitzat()) {
            throw new RuntimeException("Aquest enllaç ja ha estat utilitzat");
        }

        if (resetToken.getDataExpiracio().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Aquest enllaç ha caducat");
        }

        return resetToken;
    }

    public void marcarComUtilitzat(PasswordResetToken token) {
        token.setUtilitzat(true);
        tokenRepository.save(token);
    }
}
