/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.utils;

/**
 *
 * @author orjon
 */
public class NifValidator {
    /**
     * Executa l'operació validarDocument.
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
