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
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public String getTextDetectat() {
        return textDetectat;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param textDetectat
     */

    public void setTextDetectat(String textDetectat) {
        this.textDetectat = textDetectat;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public String getProveidorNom() {
        return proveidorNom;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param proveidorNom
     */

    public void setProveidorNom(String proveidorNom) {
        this.proveidorNom = proveidorNom;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public String getNumeroAlbara() {
        return numeroAlbara;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param numeroAlbara
     */

    public void setNumeroAlbara(String numeroAlbara) {
        this.numeroAlbara = numeroAlbara;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public String getDataRecepcio() {
        return dataRecepcio;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param dataRecepcio
     */

    public void setDataRecepcio(String dataRecepcio) {
        this.dataRecepcio = dataRecepcio;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public List<OcrLiniaDto> getLinies() {
        return linies;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param linies
     */

    public void setLinies(List<OcrLiniaDto> linies) {
        this.linies = linies;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public Long getProveidorId() {
        return proveidorId;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param proveidorId
     */

    public void setProveidorId(Long proveidorId) {
        this.proveidorId = proveidorId;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public Integer getProveidorConfidence() {
        return proveidorConfidence;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param proveidorConfidence
     */

    public void setProveidorConfidence(Integer proveidorConfidence) {
        this.proveidorConfidence = proveidorConfidence;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public String getProveidorDocument() {
        return proveidorDocument;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param proveidorDocument
     */

    public void setProveidorDocument(String proveidorDocument) {
        this.proveidorDocument = proveidorDocument;
    }

}
