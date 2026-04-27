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
import cat.copernic.easytraza.validation.CifValidator;
import cat.copernic.easytraza.validation.DniValidator;
import cat.copernic.easytraza.validation.NieValidator;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import static org.springframework.core.io.buffer.DataBufferUtils.matcher;

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

        result.setProveidorDocument(extreureDocument(text)); // o extreureCif(text), según cómo lo hayas llamado
        result.setProveidorNom(extreureNomProveidor(text));
        result.setNumeroAlbara(extreureNumeroAlbara(text));
        result.setDataRecepcio(extreureData(text));

        String[] lines = text.split("\\r?\\n");

        for (String line : lines) {
            line = line.trim();

            if (line.isEmpty()) {
                continue;
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
        if (line == null || line.trim().isEmpty()) {
            return false;
        }

        String trimmed = line.trim();
        String upper = trimmed.toUpperCase();

        if (esFiArticles(upper) || esCabeceraArticles(upper)) {
            return false;
        }

        boolean empiezaConCodigo = trimmed.matches("^([A-Z]{1,5}\\d{2,}|\\d{3,})\\s+.*");
        boolean tieneTexto = trimmed.matches(".*[A-Za-zÀ-ÿ]{3,}.*");
        boolean tieneCantidad = trimmed.matches(".*\\b\\d+[,.]?\\d*\\b.*");

        return empiezaConCodigo && tieneTexto && tieneCantidad;
    }

    /*private String extractFecha(String line) {
        if (line == null) {
            return null;
        }

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\b\\d{2}/\\d{2}/\\d{2,4}\\b");
        java.util.regex.Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            return matcher.group();
        }

        return null;
    }*/

    private OcrLiniaDto parseLinia(String line) {
        OcrLiniaDto linia = new OcrLiniaDto();

        String materia = extractMateria(line, null);
        linia.setMateriaPrimeraText(materia);

        String[] parts = line.trim().split("\\s+");
        
        // Cantidad: en este formato suele aparecer antes de "SR"
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equalsIgnoreCase("SR") && i > 0) {
                linia.setQuantitatText(parts[i - 1]);
                break;
            }
        }

        return linia;
    }

    private String extractMateria(String line, String fecha) {
        if (line == null || line.trim().isEmpty()) {
            return "";
        }

        String resultat = line.trim();

        // Quitar código inicial tipo 02173 o DS107
        resultat = resultat.replaceFirst("^[A-Z]{1,5}\\d{2,}\\s*", "");
        resultat = resultat.replaceFirst("^\\d{4,}\\s*", "");

        // Si hay fecha, cortar antes de la fecha
        if (fecha != null && !fecha.isEmpty()) {
            int posFecha = resultat.indexOf(fecha);
            if (posFecha > 0) {
                resultat = resultat.substring(0, posFecha).trim();
            }
        }

        // Cortar antes de cantidad/formato típico: "2 2,00 SR..."
        resultat = resultat.replaceFirst("\\s+\\d+\\s+\\d+[,.]\\d+\\s+SR.*$", "");

        // Cortar antes de "SR"
        resultat = resultat.replaceFirst("\\s+SR\\s+.*$", "");

        resultat = resultat.replaceAll("\\s+", " ").trim();

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
        // 1. Intentar por CIF
        if (dto.getProveidorDocument() != null && !dto.getProveidorDocument().isBlank()) {
            String docNet = dto.getProveidorDocument().trim().toUpperCase();

            Proveidor proveidor = proveidorRepo.findByCif(docNet).orElse(null);

            if (proveidor != null) {
                dto.setProveidorId(proveidor.getId());
                dto.setProveidorNom(proveidor.getNom());
                dto.setProveidorConfidence(100);
                return;
            }
        }

        // 2. Si no encuentra por CIF, fallback por nombre/texto
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
        if (textOcrNorm == null || proveidor == null || proveidor.getNom() == null) {
            return Integer.MIN_VALUE;
        }

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

    private String extreureDocument(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }

        String textNet = text.toUpperCase();

        Pattern cifPattern = Pattern.compile("\\b([A-HJNP-SUVW])\\s*[-.]?\\s*(\\d{7})\\s*[-.]?\\s*([0-9A-J])\\b");
        Matcher cifMatcher = cifPattern.matcher(textNet);

        while (cifMatcher.find()) {
            String possible = cifMatcher.group(1) + cifMatcher.group(2) + cifMatcher.group(3);

            if (CifValidator.validarCIF(possible)) {
                return possible;
            }
        }

        Pattern dniPattern = Pattern.compile("\\b(\\d{8}[A-Z])\\b");
        Matcher dniMatcher = dniPattern.matcher(textNet);

        while (dniMatcher.find()) {
            String possible = dniMatcher.group(1);

            if (DniValidator.validarDNI(possible)) {
                return possible;
            }
        }

        Pattern niePattern = Pattern.compile("\\b([XYZ]\\s*[-.]?\\s*\\d{7}\\s*[-.]?\\s*[A-Z])\\b");
        Matcher nieMatcher = niePattern.matcher(textNet);

        while (nieMatcher.find()) {
            String possible = nieMatcher.group(1)
                    .replaceAll("[\\s\\-.]", "")
                    .toUpperCase();

            if (NieValidator.validarNIE(possible)) {
                return possible;
            }
        }

        return null;
    }

    private String extreureNomProveidor(String text) {
        if (text == null) {
            return null;
        }

        String[] lines = text.split("\\r?\\n");

        for (String line : lines) {
            String l = line.trim();

            // suele estar arriba del todo
            if (l.contains("S.L") || l.contains("S.L.U") || l.contains("S.A") || l.contains("S.C.P")) {
                return l;
            }
        }

        return null;
    }

    private String extreureNumeroAlbara(String text) {
        Pattern pattern = Pattern.compile("ALBARAN\\s*(\\d+)");
        Matcher matcher = pattern.matcher(text.toUpperCase());

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    private String extreureData(String text) {
        Pattern pattern = Pattern.compile("\\b\\d{2}/\\d{2}/\\d{2,4}\\b");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group();
        }

        return null;
    }

    private boolean esCabeceraArticles(String upper) {
        return (upper.contains("CODIGO") || upper.contains("CÓDIGO") || upper.contains("ARTICULO") || upper.contains("ARTÍCULO"))
                && (upper.contains("DESCRIP") || upper.contains("CONCEPTO") || upper.contains("PRODUCTO"))
                && (upper.contains("CANTIDAD") || upper.contains("UDS") || upper.contains("SACOS") || upper.contains("LOTE"));
    }

    private boolean esFiArticles(String upper) {
        return upper.contains("ENVASES")
                || upper.contains("TOTAL")
                || upper.contains("PALET")
                || upper.contains("RECIBI")
                || upper.contains("FIRMA")
                || upper.contains("OBSERVACIONES");
    }
}
