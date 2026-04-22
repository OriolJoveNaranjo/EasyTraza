/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.service.impl;

import cat.copernic.easytraza.dto.OcrAlbaraProveidorDto;
import cat.copernic.easytraza.dto.OcrLiniaDto;
import cat.copernic.easytraza.entities.MateriaPrimera;
import cat.copernic.easytraza.entities.Proveidor;
import cat.copernic.easytraza.repository.MateriaPrimeraRepository;
import cat.copernic.easytraza.repository.ProveidorRepository;
import cat.copernic.easytraza.service.OcrService;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.text.Normalizer;
import java.util.List;
import javax.imageio.ImageIO;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author orjon
 */
@Service
public class OcrServiceImpl implements OcrService {

    private final MateriaPrimeraRepository materiaRepo;
    private final ProveidorRepository proveidorRepo;

    public OcrServiceImpl(MateriaPrimeraRepository materiaRepo, ProveidorRepository proveidorRepo) {
        this.materiaRepo = materiaRepo;
        this.proveidorRepo = proveidorRepo;
    }

    @Override
    public String extreureText(MultipartFile file) throws Exception {
        String originalName = file.getOriginalFilename();
        String extension = "png";

        if (originalName != null && originalName.lastIndexOf('.') != -1) {
            extension = originalName.substring(originalName.lastIndexOf('.') + 1);
        }

        File tempOriginal = File.createTempFile("ocr-original-", "." + extension);
        File tempProcessed = File.createTempFile("ocr-processed-", ".png");

        file.transferTo(tempOriginal);

        BufferedImage originalImage = ImageIO.read(tempOriginal);
        if (originalImage == null) {
            throw new RuntimeException("No s'ha pogut llegir la imatge");
        }

        BufferedImage processedImage = preprocessarImatge(originalImage);
        ImageIO.write(processedImage, "png", tempProcessed);

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
        tesseract.setLanguage("spa+eng");
        tesseract.setPageSegMode(6);
        String resultat;
        try {
            resultat = tesseract.doOCR(tempProcessed);
        } finally {
            tempOriginal.delete();
            tempProcessed.delete();
        }

        return resultat;
    }

    @Override
    public OcrAlbaraProveidorDto processarAlbara(MultipartFile file) throws Exception {
        String text = extreureText(file);
        OcrAlbaraProveidorDto dto = parseText(text);
        return resoldreEntitats(dto);
    }

    private OcrAlbaraProveidorDto parseText(String text) {
        OcrAlbaraProveidorDto result = new OcrAlbaraProveidorDto();
        result.setTextDetectat(text);

        String[] lines = text.split("\\r?\\n");

        for (String line : lines) {
            line = line.trim();

            if (line.contains("ALBARAN")) {
                String[] parts = line.split(" ");
                for (String part : parts) {
                    if (part.matches("[A-Z]{2,}\\d+")) {
                        result.setNumeroAlbara(part);
                    }
                }
            }

            if (result.getDataRecepcio() == null) {
                String fecha = extractFecha(line);
                if (fecha != null) {
                    result.setDataRecepcio(fecha);
                }
            }

            if (esLiniaProducte(line)) {
                OcrLiniaDto linia = parseLinia(line);
                if (linia != null) {
                    result.getLinies().add(linia);
                }
            }
        }

        return result;
    }

    private boolean esLiniaProducte(String line) {
        if (line == null) {
            return false;
        }

        String trimmed = line.trim();
        if (!trimmed.matches("^\\d{4,}.*")) {
            return false;
        }

        boolean teData = trimmed.matches(".*\\d{2}/\\d{2}/\\d{4}.*");
        boolean teLoteProbable = trimmed.matches(".*\\b[A-Z]{1,3}\\d+[A-Z0-9]*\\b.*")
                || trimmed.matches(".*\\b[A-Z0-9]{6,}\\b.*");

        String upper = trimmed.toUpperCase();

        boolean esCabecera = upper.contains("NIF")
                || upper.contains("N.I.F")
                || upper.contains("ALBARAN")
                || upper.contains("ALBARÁN")
                || upper.contains("FECHA")
                || upper.contains("CLIENTE")
                || upper.contains("PAG")
                || upper.contains("PÀG")
                || upper.contains("CP")
                || upper.contains("CODI POSTAL");

        return teData && teLoteProbable && !esCabecera;
    }

    private String extractFecha(String line) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\d{2}/\\d{2}/\\d{4}");
        java.util.regex.Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            return matcher.group();
        }

        return null;
    }

    private OcrLiniaDto parseLinia(String line) {
        OcrLiniaDto linia = new OcrLiniaDto();

        String[] parts = line.trim().split("\\s+");

        String fecha = null;
        String lote = null;
        String cantidad = null;

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];

            // fecha tipo 26/07/2026 o incluso mal OCR parecida
            if (part.matches("\\d{2}/\\d{2}/\\d{4}") || part.matches("\\d{2}/\\d{2}/\\d{5}")) {
                fecha = part;

                if (i + 1 < parts.length) {
                    lote = parts[i + 1];
                }
            }
        }

        // cantidad: buscamos el entero más cercano al final, evitando código inicial
        for (int i = parts.length - 1; i >= 0; i--) {
            String part = parts[i];

            if (part.matches("\\d+") && i > 0) {
                cantidad = part;
                break;
            }
        }

        linia.setDataCaducitat(fecha);
        linia.setIdentificadorLot(lote);
        linia.setQuantitatText(cantidad);
        linia.setMateriaPrimeraText(extractMateria(line, fecha));

        return linia;
    }

    private String extractMateria(String line, String fecha) {
        if (line == null || line.trim().isEmpty()) {
            return "";
        }

        String resultat = line.trim();

        // 1. quitar código inicial tipo 02173
        resultat = resultat.replaceFirst("^\\d{4,}\\s*", "");

        // 2. cortar cuando empieza la fecha
        if (fecha != null && !fecha.isEmpty()) {
            int posFecha = resultat.indexOf(fecha);
            if (posFecha > 0) {
                resultat = resultat.substring(0, posFecha).trim();
            }
        }

        // 3. limpiar espacios repetidos
        resultat = resultat.replaceAll("\\s+", " ").trim();

        // 4. eliminar caracteres raros al principio/final
        resultat = resultat.replaceAll("^[^A-Za-zÀ-ÿ0-9]+", "");
        resultat = resultat.replaceAll("[^A-Za-zÀ-ÿ0-9. ]+$", "");

        return resultat;
    }

    private BufferedImage preprocessarImatge(BufferedImage original) {
        int newWidth = original.getWidth() * 2;
        int newHeight = original.getHeight() * 2;

        Image scaled = original.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = scaledImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.drawImage(scaled, 0, 0, null);
        g2d.dispose();

        BufferedImage grayImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        op.filter(scaledImage, grayImage);

        BufferedImage binaryImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_BINARY);

        for (int x = 0; x < grayImage.getWidth(); x++) {
            for (int y = 0; y < grayImage.getHeight(); y++) {
                int rgb = grayImage.getRGB(x, y) & 0xFF;
                int color = (rgb > 140) ? 0xFFFFFF : 0x000000;
                binaryImage.setRGB(x, y, color);
            }
        }

        return binaryImage;
    }

    private OcrAlbaraProveidorDto resoldreEntitats(OcrAlbaraProveidorDto dto) {
        resoldreProveidor(dto);

        if (dto.getLinies() != null) {
            for (OcrLiniaDto linia : dto.getLinies()) {
                linia.setMateriaPrimeraId(trobarOCrearMateriaPrimera(linia.getMateriaPrimeraText()));
            }
        }

        return dto;
    }

    private void resoldreProveidor(OcrAlbaraProveidorDto dto) {
        if (dto.getTextDetectat() == null || dto.getTextDetectat().trim().isEmpty()) {
            dto.setProveidorId(null);
            dto.setProveidorConfidence(0);
            return;
        }

        String textOcrNorm = normalitzar(dto.getTextDetectat());

        Proveidor millorProveidor = null;
        int millorScore = Integer.MIN_VALUE;

        List<Proveidor> proveidors = proveidorRepo.findAll();

        for (Proveidor proveidor : proveidors) {
            if (proveidor.getNom() == null || proveidor.getNom().trim().isEmpty()) {
                continue;
            }

            int score = puntuarProveidorEnTextComplet(textOcrNorm, proveidor);

            if (score > millorScore) {
                millorScore = score;
                millorProveidor = proveidor;
            }
        }

        if (millorProveidor != null && millorScore >= 8) {
            dto.setProveidorId(millorProveidor.getId());
            dto.setProveidorNom(millorProveidor.getNom());
            dto.setProveidorConfidence(millorScore);
        } else {
            dto.setProveidorId(null);
            dto.setProveidorConfidence(Math.max(millorScore, 0));
        }
    }

    private int puntuarProveidorEnTextComplet(String textOcrNorm, Proveidor proveidor) {
        String nomNorm = normalitzar(proveidor.getNom());

        if (nomNorm.isEmpty()) {
            return Integer.MIN_VALUE;
        }

        int score = 0;

        if (textOcrNorm.contains(nomNorm)) {
            score += 20;
        }

        String[] paraules = nomNorm.split(" ");
        int coincidencies = 0;

        for (String paraula : paraules) {
            if (paraula.length() < 3) {
                continue;
            }
            if (textOcrNorm.contains(paraula)) {
                coincidencies++;
            }
        }

        score += coincidencies * 4;

        if (coincidencies == paraulesSignificatives(nomNorm)) {
            score += 8;
        }

        return score;
    }

    private int paraulesSignificatives(String text) {
        int count = 0;
        for (String p : text.split(" ")) {
            if (p.length() >= 3) {
                count++;
            }
        }
        return count;
    }

    private Long trobarOCrearMateriaPrimera(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }

        String textNet = text.trim();
        String normalitzat = normalitzar(textNet);

        List<MateriaPrimera> materies = materiaRepo.findAll();

        MateriaPrimera millor = null;
        int millorScore = Integer.MIN_VALUE;

        for (MateriaPrimera materia : materies) {
            if (materia.getNom() == null || materia.getNom().trim().isEmpty()) {
                continue;
            }

            String nomBd = normalitzar(materia.getNom());
            int score = puntuarMateria(normalitzat, nomBd);

            if (score > millorScore) {
                millorScore = score;
                millor = materia;
            }
        }

        if (millor != null && millorScore >= 8) {
            return millor.getId();
        }

        MateriaPrimera nova = new MateriaPrimera();
        nova.setNom(textNet);

        return materiaRepo.save(nova).getId();
    }

    private int puntuarMateria(String ocr, String bd) {
        int score = 0;

        if (ocr.equals(bd)) {
            score += 20;
        }

        if (ocr.contains(bd) || bd.contains(ocr)) {
            score += 10;
        }

        int compartides = comptarParaulesCompartides(ocr, bd);
        score += compartides * 3;

        if (ocr.length() < 3 || bd.length() < 3) {
            score -= 5;
        }

        return score;
    }

    private int comptarParaulesCompartides(String a, String b) {
        String[] paraulesA = a.split(" ");
        String[] paraulesB = b.split(" ");

        int count = 0;

        for (String pa : paraulesA) {
            if (pa.length() < 3) {
                continue;
            }

            for (String pb : paraulesB) {
                if (pa.equals(pb)) {
                    count++;
                    break;
                }
            }
        }

        return count;
    }

    private String normalitzar(String text) {
        return text == null ? ""
                : Normalizer.normalize(text.toLowerCase(), Normalizer.Form.NFD)
                        .replaceAll("\\p{M}", "")
                        .replaceAll("[^a-z0-9 ]", " ")
                        .replaceAll("\\s+", " ")
                        .trim();
    }

}
