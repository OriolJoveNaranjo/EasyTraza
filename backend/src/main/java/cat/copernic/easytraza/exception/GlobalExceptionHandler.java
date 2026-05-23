package cat.copernic.easytraza.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Gestiona les excepcions globals de l'aplicació web i registra els errors
 * rellevants als fitxers de log per complir el requisit RN08.
 *
 * <p>Els errors de càrrega de fitxers retornen una resposta HTTP perquè poden
 * provenir de formularis o peticions asíncrones. Els errors de base de dades i
 * errors generals mostren una pàgina d'error personalitzada.</p>
 *
 * @author orjon
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Gestiona la pujada d'imatges o fitxers massa grans.
     *
     * @param e excepció generada pel multipart resolver
     * @return resposta HTTP 413 amb missatge llegible
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException e) {
        logger.warn("S'ha intentat pujar un fitxer que supera el límit permès", e);

        return ResponseEntity
                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body("La imatge supera el límit permès de 20MB");
    }

    /**
     * Gestiona errors d'accés a dades, com una caiguda de MySQL o errors de
     * connexió amb la base de dades.
     *
     * @param e excepció de persistència o accés a dades
     * @param request petició que ha provocat l'error
     * @param model model Thymeleaf per mostrar la pàgina d'error
     * @return plantilla d'error personalitzada
     */
    @ExceptionHandler(DataAccessException.class)
    public String handleDatabaseError(
            DataAccessException e,
            HttpServletRequest request,
            Model model
    ) {
        logger.error("Error de base de dades a la ruta {}", request.getRequestURI(), e);

        model.addAttribute("titol", "Error de connexió amb la base de dades");
        model.addAttribute("missatge", "No s'ha pogut accedir a la base de dades. Revisa que MySQL estigui en marxa.");
        model.addAttribute("ruta", request.getRequestURI());

        return "error-personalitzat";
    }

    /**
     * Gestiona qualsevol error no controlat perquè l'usuari no vegi una pàgina
     * tècnica de Spring i perquè l'administrador pugui consultar el log.
     *
     * @param e excepció no controlada
     * @param request petició que ha provocat l'error
     * @param model model Thymeleaf per mostrar la pàgina d'error
     * @return plantilla d'error personalitzada
     */
    @ExceptionHandler(Exception.class)
    public String handleGeneralError(
            Exception e,
            HttpServletRequest request,
            Model model
    ) {
        logger.error("Error inesperat a la ruta {}", request.getRequestURI(), e);

        model.addAttribute("titol", "S'ha produït un error");
        model.addAttribute("missatge", "Hi ha hagut un problema inesperat. Torna-ho a provar més tard.");
        model.addAttribute("ruta", request.getRequestURI());

        return "error-personalitzat";
    }
}
