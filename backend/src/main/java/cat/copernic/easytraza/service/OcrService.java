/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
