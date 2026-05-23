package cat.copernic.easytraza.service;

import cat.copernic.easytraza.dto.OcrAlbaraProveidorDto;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author orjon
 */
public interface OcrService {

    String extreureText(MultipartFile file) throws Exception;

    OcrAlbaraProveidorDto processarAlbara(MultipartFile file) throws Exception;
}
