/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.controller;

import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author orjon
 */
public class ErrorController {

    @GetMapping("/acces-denegat")
    public String accesDenegat() {
        return "acces-denegat";
    }
}
