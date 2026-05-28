package cat.copernic.easytraza.utils;

/**
 *
 * @author orjon
 */
public class DniValidator {
    /**
     * Executa l'operació validarDNI.
     * @param dni
     * @return 
     */

    public static boolean validarDNI(String dni) {
        if (dni == null || !dni.matches("^\\d{8}[A-Z]$")) {
            return false;
        }

        String lletres = "TRWAGMYFPDXBNJZSQVHLCKE";
        int numero = Integer.parseInt(dni.substring(0, 8));
        char lletraCorrecta = lletres.charAt(numero % 23);

        return dni.charAt(8) == lletraCorrecta;
    }
}
