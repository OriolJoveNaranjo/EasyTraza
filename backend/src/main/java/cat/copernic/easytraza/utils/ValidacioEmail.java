package cat.copernic.easytraza.utils;

/**
 *
 * @author orjon
 */
public class ValidacioEmail {

    private static final String EMAIL_REGEX
            = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    /**
     * Executa l'operació emailNoValid.
     */

    public static boolean emailNoValid(String email) {
        String emailNet = email == null ? "" : email.trim();

        if (emailNet.isEmpty()) {
            return false;
        }

        return !email.matches(EMAIL_REGEX);
    }
}
