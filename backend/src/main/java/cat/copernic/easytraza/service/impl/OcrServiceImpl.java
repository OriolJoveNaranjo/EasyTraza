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
import cat.copernic.easytraza.utils.CifValidator;
import cat.copernic.easytraza.utils.DniValidator;
import cat.copernic.easytraza.utils.NieValidator;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 *
 * @author orjon
 */
@Service
public class OcrServiceImpl implements OcrService {

    private final MateriaPrimeraRepository materiaRepo;
    private final ProveidorRepository proveidorRepo;
    /**
     * Executa l'operació OcrServiceImpl.
     * @param materiaRepo
     * @param proveidorRepo
     */

    public OcrServiceImpl(MateriaPrimeraRepository materiaRepo, ProveidorRepository proveidorRepo) {
        this.materiaRepo = materiaRepo;
        this.proveidorRepo = proveidorRepo;
    }
    /**
     * Executa l'operació extreureText.
     * @param file
     * @return 
     * @throws java.lang.Exception
     */

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
    /**
     * Executa l'operació processarAlbara.
     * @param file
     * @return 
     * @throws java.lang.Exception
     */

    @Override
    public OcrAlbaraProveidorDto processarAlbara(MultipartFile file) throws Exception {
        String text = extreureText(file);
        OcrAlbaraProveidorDto dto = parseText(text);
        return resoldreEntitats(dto);
    }

    private OcrAlbaraProveidorDto parseText(String text) {
        OcrAlbaraProveidorDto result = new OcrAlbaraProveidorDto();
        result.setTextDetectat(text);

        result.setProveidorDocument(extreureDocument(text));
        result.setProveidorNom(extreureNomProveidor(text));
        result.setNumeroAlbara(extreureNumeroAlbara(text));
        result.setDataRecepcio(extreureData(text));

        String[] lines = text.split("\\r?\\n");

        boolean enZonaArticles = false;
        boolean haTrobatCapcaleraArticles = false;

        for (String line : lines) {
            line = line.trim();

            if (line.isEmpty()) {
                continue;
            }

            String upper = line.toUpperCase();

            if (esCabeceraArticles(upper)) {
                enZonaArticles = true;
                haTrobatCapcaleraArticles = true;
                continue;
            }

            if (enZonaArticles && esFiArticles(upper)) {
                enZonaArticles = false;
                continue;
            }

            if (!enZonaArticles) {
                continue;
            }

            if (esLiniaProducte(line)) {
                OcrLiniaDto linia = parseLinia(line);
                if (linia != null) {
                    result.getLinies().add(linia);
                }
            }
        }

        // Fallback: si no ha detectado cabecera o no ha sacado líneas,
        // intenta detectar productos en todo el texto
        if (!haTrobatCapcaleraArticles || result.getLinies().isEmpty()) {
            result.getLinies().clear();

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

    private String extractFecha(String line) {
        if (line == null) {
            return null;
        }

        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\b\\d{2}/\\d{2}/\\d{2,4}\\b");
        java.util.regex.Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            return matcher.group();
        }

        return null;
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
        return (upper.contains("CODIGO")
                || upper.contains("CODI")
                || upper.contains("CÓDIGO")
                || upper.contains("ARTICULO")
                || upper.contains("ARTÍCULO")
                || upper.contains("CODI"))
                && (upper.contains("DESCRIP")
                || upper.contains("CONCEPTO")
                || upper.contains("PRODUCTO")
                || upper.contains("ARTICLE")
                || upper.contains("ARTICULO"))
                && (upper.contains("CANT")
                || upper.contains("UDS")
                || upper.contains("SACOS")
                || upper.contains("LOTE")
                || upper.contains("UNITAT")
                || upper.contains("U.M"));
    }

    private boolean esFiArticles(String upper) {
        return upper.contains("ENVASES")
                || upper.contains("TOTAL")
                || upper.contains("PALET")
                || upper.contains("RECIBI")
                || upper.contains("FIRMA")
                || upper.contains("OBSERVACIONES");
    }

    private OcrLiniaDto parseLiniaProducte(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String net = line.trim()
                .replace("—", " ")
                .replaceAll("\\s+", " ");

        String[] parts = net.split("\\s+");

        if (parts.length < 3) {
            return null;
        }

        String codiArticle = parts[0];

        if (!codiArticle.matches("^[A-Z]{0,5}\\d{2,}$")) {
            return null;
        }

        OcrLiniaDto dto = new OcrLiniaDto();
        dto.setIdentificadorLot(codiArticle);

        String data = extractFecha(net);
        dto.setDataCaducitat(data);

        int indexFiNom = trobarIndexFiNom(parts, data);

        if (indexFiNom <= 1) {
            return null;
        }

        StringBuilder nom = new StringBuilder();

        for (int i = 1; i < indexFiNom; i++) {
            if (nom.length() > 0) {
                nom.append(" ");
            }
            nom.append(parts[i]);
        }

        dto.setMateriaPrimeraText(nom.toString().trim());

        String unitat = trobarUnitat(parts);
        dto.setUnitat(unitat);

        String quantitat = trobarQuantitat(parts, unitat);
        dto.setQuantitatText(quantitat);

        return dto;
    }

    private int trobarIndexFiNom(String[] parts, String data) {
        for (int i = 1; i < parts.length; i++) {
            String p = parts[i].toUpperCase();

            if (data != null && parts[i].equals(data)) {
                return i;
            }

            if (esUnitatOEnvase(p)) {
                return i;
            }

            if (p.matches("\\d+[,.]?\\d*") && i > 2) {
                return i;
            }
        }

        return parts.length;
    }

    private boolean esUnitatOEnvase(String text) {
        if (text == null) {
            return false;
        }

        String t = text.toUpperCase();

        return t.equals("KG")
                || t.equals("L")
                || t.equals("CAIXA")
                || t.equals("CAIXES")
                || t.equals("UNITAT")
                || t.equals("UNITATS")
                || t.equals("SACS")
                || t.equals("SACOS")
                || t.startsWith("SACO")
                || t.startsWith("SAC")
                || t.equals("TONELADES")
                || t.equals("SR");
    }

    private String trobarUnitat(String[] parts) {
        for (String part : parts) {
            String p = part.toUpperCase();

            if (p.equals("CAIXA") || p.equals("CAIXES")) {
                return "CAIXES";
            }

            if (p.equals("UNITAT") || p.equals("UNITATS")) {
                return "UNITATS";
            }

            if (p.equals("KG")) {
                return "KG";
            }

            if (p.equals("L")) {
                return "L";
            }

            if (p.equals("TONELADES")) {
                return "TONELADES";
            }

            if (p.equals("SACS") || p.equals("SACOS") || p.startsWith("SACO") || p.startsWith("SAC")) {
                return "SACS";
            }
        }

        return null;
    }

    private String trobarQuantitat(String[] parts, String unitat) {
        for (int i = 0; i < parts.length; i++) {
            String p = parts[i].toUpperCase();

            if (esUnitatOEnvase(p) && i + 1 < parts.length) {
                String possible = parts[i + 1];

                if (possible.matches("\\d+[,.]?\\d*")) {
                    return possible;
                }
            }
        }

        return null;
    }

    private OcrLiniaDto parseLinia(String line) {
        OcrLiniaDto linia = parseLiniaProducte(line);

        if (linia != null) {
            return linia;
        }

        return null;
    }
}
