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
    private String unitat;
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public String getMateriaPrimeraText() {
        return materiaPrimeraText;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param materiaPrimeraText
     */

    public void setMateriaPrimeraText(String materiaPrimeraText) {
        this.materiaPrimeraText = materiaPrimeraText;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public String getDataCaducitat() {
        return dataCaducitat;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param dataCaducitat
     */

    public void setDataCaducitat(String dataCaducitat) {
        this.dataCaducitat = dataCaducitat;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public String getIdentificadorLot() {
        return identificadorLot;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param identificadorLot
     */

    public void setIdentificadorLot(String identificadorLot) {
        this.identificadorLot = identificadorLot;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public String getQuantitatText() {
        return quantitatText;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param quantitatText
     */

    public void setQuantitatText(String quantitatText) {
        this.quantitatText = quantitatText;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public Long getMateriaPrimeraId() {
        return materiaPrimeraId;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param materiaPrimeraId
     */

    public void setMateriaPrimeraId(Long materiaPrimeraId) {
        this.materiaPrimeraId = materiaPrimeraId;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public String getUnitat() {
        return unitat;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param unitat
     */

    public void setUnitat(String unitat) {
        this.unitat = unitat;
    }
    
    
}
