package cat.copernic.easytraza.service;

import cat.copernic.easytraza.entities.PasswordResetToken;
import cat.copernic.easytraza.entities.Usuari;
import cat.copernic.easytraza.repository.AlbaraProveidorRepository;
import cat.copernic.easytraza.repository.ControlPhRepository;
import cat.copernic.easytraza.repository.LotProveidorRepository;
import cat.copernic.easytraza.repository.PasswordResetTokenRepository;
import cat.copernic.easytraza.repository.UsuariRepository;
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
    private final UsuariRepository userRepo;
    private final LotProveidorRepository lotProveidorRepo;
    private final AlbaraProveidorRepository albaraProveidorRepo;
    private final ControlPhRepository controlPhRepo;
    /**
     * Executa l'operació PasswordResetService.
     * @param tokenRepository
     * @param userRepo
     * @param lotProveidorRepo
     * @param albaraProveidorRepo
     * @param controlPhRepo
     */

    public PasswordResetService(PasswordResetTokenRepository tokenRepository, UsuariRepository userRepo,
            LotProveidorRepository lotProveidorRepo, AlbaraProveidorRepository albaraProveidorRepo, ControlPhRepository controlPhRepo) {
        this.tokenRepository = tokenRepository;
        this.userRepo = userRepo;
        this.albaraProveidorRepo = albaraProveidorRepo;
        this.controlPhRepo = controlPhRepo;
        this.lotProveidorRepo = lotProveidorRepo;
    }
    /**
     * Executa l'operació crearToken.
     * @param usuari
     * @return 
     */

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
    /**
     * Executa l'operació validarToken.
     * @param token
     * @return 
     */

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
    /**
     * Executa l'operació marcarComUtilitzat.
     * @param token
     */

    public void marcarComUtilitzat(PasswordResetToken token) {
        token.setUtilitzat(true);
        tokenRepository.save(token);
    }
    /**
     * Elimina o desactiva el registre indicat segons les regles de negoci.
     * @param id
     * @return 
     */

    @Transactional
    public String deleteById(Long id) {
        Usuari usuari = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuari no trobat"));

        boolean teLots = lotProveidorRepo.existsByUsuariOberturaId(id);
        boolean teAlbaransProveidor = albaraProveidorRepo.existsByUsuariAltaId(id);
        boolean teControls = controlPhRepo.existsByUsuariId(id);

        boolean teDadesImportants = teLots || teAlbaransProveidor || teControls;

        tokenRepository.deleteByUsuariId(id);

        if (teDadesImportants) {
            usuari.setActiu(false);
            userRepo.save(usuari);
            return "Aquest usuari no es pot eliminar perquè té dades associades, però s'ha desactivat.";
        }

        userRepo.deleteById(id);
        return "Usuari eliminat correctament.";
    }
}
