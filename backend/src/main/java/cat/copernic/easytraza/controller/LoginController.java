/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author orjon Controlador para mostrar la pantalla de login.
 */
@Controller
public class LoginController {
    /**
     * Executa l'operació login.
     */

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
