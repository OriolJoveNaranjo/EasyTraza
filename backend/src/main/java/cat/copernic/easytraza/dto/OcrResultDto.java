package cat.copernic.easytraza.dto;

/**
 *
 * @author orjon
 */
public class OcrResultDto {

    private String textDetectat;
    /**
     * Executa l'operació OcrResultDto.
     */

    public OcrResultDto() {
    }
    /**
     * Executa l'operació OcrResultDto.
     */

    public OcrResultDto(String textDetectat) {
        this.textDetectat = textDetectat;
    }
    /**
     * Retorna el valor de la propietat indicada.
     */

    public String getTextDetectat() {
        return textDetectat;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     */

    public void setTextDetectat(String textDetectat) {
        this.textDetectat = textDetectat;
    }
}
