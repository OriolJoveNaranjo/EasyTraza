/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.utils;

/**
 *
 * @author orjon
 */
public class NieValidator {

    public static boolean validarNIE(String nie) {
        if (nie == null || !nie.matches("^[XYZ]\\d{7}[A-Z]$")) {
            return false;
        }

        String inici = nie.substring(0, 1);
        String resta = nie.substring(1, 8);
        String lletra = nie.substring(8);

        String numeroNie = switch (inici) {
            case "X" -> "0" + resta;
            case "Y" -> "1" + resta;
            case "Z" -> "2" + resta;
            default -> "";
        };

        String lletres = "TRWAGMYFPDXBNJZSQVHLCKE";
        int numero = Integer.parseInt(numeroNie);
        char lletraCorrecta = lletres.charAt(numero % 23);

        return lletra.equals(String.valueOf(lletraCorrecta));
    }
}
