package cat.copernic.easytraza.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Servei encarregat de l'enviament de correus electrònics de l'aplicació.
 *
 * <p>Actualment s'utilitza per enviar l'enllaç de recuperació de contrasenya.
 * Els errors d'enviament queden registrats als fitxers de log per facilitar el
 * manteniment i la diagnosi d'incidències.</p>
 *
 * @author orjon
 */
@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    /**
     * Crea el servei d'enviament de correus.
     *
     * @param mailSender component de Spring encarregat d'enviar emails
     */
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Envia un enllaç de recuperació de contrasenya a l'usuari indicat.
     *
     * @param destinatari adreça de correu de l'usuari
     * @param enllac URL segura amb el token de recuperació
     */
    public void enviarEnllacRecuperacio(String destinatari, String enllac) {
        SimpleMailMessage missatge = new SimpleMailMessage();

        missatge.setFrom("jove.naranjo.oriol@alumnat.copernic.cat");
        missatge.setTo(destinatari);
        missatge.setSubject("Recuperació de contrasenya - EasyTraza");
        missatge.setText(
                "Has sol·licitat recuperar la teva contrasenya.\n\n"
                + "Fes clic en aquest enllaç per crear una nova contrasenya:\n"
                + enllac + "\n\n"
                + "Aquest enllaç caduca en 15 minuts.\n\n"
                + "Si no has sol·licitat aquest canvi, ignora aquest correu."
        );

        try {
            mailSender.send(missatge);
            logger.info("Correu de recuperació enviat a {}", destinatari);
        } catch (Exception e) {
            logger.error("No s'ha pogut enviar el correu de recuperació a {}", destinatari, e);
        }
    }
}
