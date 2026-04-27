/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author orjon
 */
public class OcrAlbaraProveidorDto {

    private Long proveidorId;
    private String proveidorDocument;
    private String textDetectat;
    private String proveidorNom;
    private String numeroAlbara;
    private String dataRecepcio;
    private Integer proveidorConfidence;
    private List<OcrLiniaDto> linies = new ArrayList<>();

    public String getTextDetectat() {
        return textDetectat;
    }

    public void setTextDetectat(String textDetectat) {
        this.textDetectat = textDetectat;
    }

    public String getProveidorNom() {
        return proveidorNom;
    }

    public void setProveidorNom(String proveidorNom) {
        this.proveidorNom = proveidorNom;
    }

    public String getNumeroAlbara() {
        return numeroAlbara;
    }

    public void setNumeroAlbara(String numeroAlbara) {
        this.numeroAlbara = numeroAlbara;
    }

    public String getDataRecepcio() {
        return dataRecepcio;
    }

    public void setDataRecepcio(String dataRecepcio) {
        this.dataRecepcio = dataRecepcio;
    }

    public List<OcrLiniaDto> getLinies() {
        return linies;
    }

    public void setLinies(List<OcrLiniaDto> linies) {
        this.linies = linies;
    }

    public Long getProveidorId() {
        return proveidorId;
    }

    public void setProveidorId(Long proveidorId) {
        this.proveidorId = proveidorId;
    }

    public Integer getProveidorConfidence() {
        return proveidorConfidence;
    }

    public void setProveidorConfidence(Integer proveidorConfidence) {
        this.proveidorConfidence = proveidorConfidence;
    }

    public String getProveidorDocument() {
        return proveidorDocument;
    }

    public void setProveidorDocument(String proveidorDocument) {
        this.proveidorDocument = proveidorDocument;
    }

}
