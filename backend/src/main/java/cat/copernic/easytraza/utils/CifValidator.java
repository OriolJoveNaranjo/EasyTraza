/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.utils;

/**
 *
 * @author orjon
 */
public class CifValidator {

    public static boolean validarCIF(String cif) {
        if (cif == null || !cif.matches("^[A-HJNP-SUVW][0-9]{7}[0-9A-J]$")) {
            return false;
        }

        char control = cif.charAt(8);
        String numeros = cif.substring(1, 8);

        int sumaPar = 0;
        int sumaImpar = 0;

        for (int i = 0; i < numeros.length(); i++) {
            int num = Character.getNumericValue(numeros.charAt(i));

            if (i % 2 == 0) {
                int resultado = num * 2;
                sumaImpar += (resultado / 10) + (resultado % 10);
            } else {
                sumaPar += num;
            }
        }

        int total = sumaPar + sumaImpar;
        int digitoControl = (10 - (total % 10)) % 10;
        char letraControl = "JABCDEFGHI".charAt(digitoControl);

        if (Character.isDigit(control)) {
            return control == Character.forDigit(digitoControl, 10);
        } else {
            return control == letraControl;
        }
    }
}
