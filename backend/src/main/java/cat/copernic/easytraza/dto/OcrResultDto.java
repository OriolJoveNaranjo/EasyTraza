/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.dto;

/**
 *
 * @author orjon
 */
public class OcrResultDto {

    private String textDetectat;

    public OcrResultDto() {
    }

    public OcrResultDto(String textDetectat) {
        this.textDetectat = textDetectat;
    }

    public String getTextDetectat() {
        return textDetectat;
    }

    public void setTextDetectat(String textDetectat) {
        this.textDetectat = textDetectat;
    }
}
