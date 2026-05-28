package cat.copernic.easytraza.utils;

/**
 *
 * @author orjon
 */
public class NifValidator {
    /**
     * Executa l'operació validarDocument.
     * @param document
     * @return 
     */

    public static boolean validarDocument(String document) {
        if (document == null) {
            return false;
        }

        String net = document.trim().toUpperCase().replace(" ", "");

        return DniValidator.validarDNI(net)
                || NieValidator.validarNIE(net)
                || CifValidator.validarCIF(net);
    }
}
