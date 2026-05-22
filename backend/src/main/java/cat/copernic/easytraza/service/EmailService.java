package cat.copernic.easytraza.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author orjon Servicio para enviar correos electrónicos.
 */
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    

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
            System.out.println("Correu enviat a: " + destinatari);
        } catch (Exception e) {
            System.out.println("ERROR ENVIANT CORREU:");
            e.printStackTrace();
        }
    }
}
