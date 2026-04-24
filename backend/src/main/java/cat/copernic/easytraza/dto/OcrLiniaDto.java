/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.dto;


/**
 *
 * @author orjon
 */
public class OcrLiniaDto {

    private Long materiaPrimeraId;
    private String materiaPrimeraText;
    private String dataCaducitat;
    private String identificadorLot;
    private String quantitatText;

    public String getMateriaPrimeraText() {
        return materiaPrimeraText;
    }

    public void setMateriaPrimeraText(String materiaPrimeraText) {
        this.materiaPrimeraText = materiaPrimeraText;
    }

    public String getDataCaducitat() {
        return dataCaducitat;
    }

    public void setDataCaducitat(String dataCaducitat) {
        this.dataCaducitat = dataCaducitat;
    }

    public String getIdentificadorLot() {
        return identificadorLot;
    }

    public void setIdentificadorLot(String identificadorLot) {
        this.identificadorLot = identificadorLot;
    }

    public String getQuantitatText() {
        return quantitatText;
    }

    public void setQuantitatText(String quantitatText) {
        this.quantitatText = quantitatText;
    }

    public Long getMateriaPrimeraId() {
        return materiaPrimeraId;
    }

    public void setMateriaPrimeraId(Long materiaPrimeraId) {
        this.materiaPrimeraId = materiaPrimeraId;
    }
    
}
