package cat.copernic.easytraza.service.impl;

import cat.copernic.easytraza.entities.Proveidor;
import cat.copernic.easytraza.repository.AlbaraProveidorRepository;
import cat.copernic.easytraza.repository.ProveidorRepository;
import cat.copernic.easytraza.service.ProveidorService;
import cat.copernic.easytraza.utils.CifValidator;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Implementació de la lògica de negoci associada als proveïdors.
 *
 * <p>Centralitza validacions com el CIF, l'activació/desactivació i el control
 * de proveïdors amb albarans associats abans d'eliminar-los.</p>
 */
@Service
public class ProveidorServiceImpl implements ProveidorService {

    private final ProveidorRepository proveidorRepository;
    private final AlbaraProveidorRepository alabaraProveidorRepo;

    public ProveidorServiceImpl(ProveidorRepository proveidorRepository, AlbaraProveidorRepository alabaraProveidorRepo) {
        this.proveidorRepository = proveidorRepository;
        this.alabaraProveidorRepo = alabaraProveidorRepo;
    }

    @Override
    public List<Proveidor> findAll() {
        return proveidorRepository.findAll();
    }

    @Override
    public Optional<Proveidor> findById(Long id) {
        return proveidorRepository.findById(id);
    }

    @Override
    public Proveidor save(Proveidor proveidor) {
        validarProveidor(proveidor);

        String cifNet = normalitzarCif(proveidor.getCif());
        proveidor.setCif(cifNet);

        if (proveidorRepository.existsByCif(cifNet)) {
            throw new RuntimeException("Ja existeix un proveïdor amb aquest NIF/CIF");
        }

        return proveidorRepository.save(proveidor);
    }

    @Override
    public Proveidor update(Long id, Proveidor proveidor) {
        Proveidor actual = proveidorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El proveïdor no existeix"));

        validarProveidor(proveidor);

        String cifNet = normalitzarCif(proveidor.getCif());
        proveidor.setCif(cifNet);

        if (proveidorRepository.existsByCifAndIdNot(cifNet, id)) {
            throw new RuntimeException("Ja existeix un proveïdor amb aquest NIF/CIF");
        }

        actual.setNom(netText(proveidor.getNom()));
        actual.setCif(cifNet);
        actual.setTelefon(netText(proveidor.getTelefon()));
        actual.setEmail(netText(proveidor.getEmail()));
        actual.setAdreca(netText(proveidor.getAdreca()));
        actual.setObservacions(netText(proveidor.getObservacions()));

        return proveidorRepository.save(actual);
    }

    @Override
    public String deleteById(Long id) {
        Proveidor proveidor = proveidorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveïdor no trobat"));

        boolean teAlbarans = alabaraProveidorRepo.existsByProveidorId(id);

        if (teAlbarans) {
            proveidor.setActiu(false);
            proveidorRepository.save(proveidor);
            return "Aquest proveïdor no es pot eliminar perquè té dades associades, però s'ha desactivat.";
        }

        proveidorRepository.deleteById(id);
        return "Proveïdor eliminat correctament.";
    }

    private void validarProveidor(Proveidor proveidor) {
        if (proveidor.getNom() == null || proveidor.getNom().trim().isEmpty()) {
            throw new RuntimeException("El nom és obligatori");
        }

        if (proveidor.getCif() == null || proveidor.getCif().trim().isEmpty()) {
            throw new RuntimeException("El NIF/CIF és obligatori");
        }

        String cifNet = normalitzarCif(proveidor.getCif());
        proveidor.setCif(cifNet);

        if (proveidor.getObservacions() != null && proveidor.getObservacions().trim().length() > 500) {
            throw new IllegalArgumentException("El text és massa gran. Com a màxim 500 caràcters");
        }

        proveidor.setNom(proveidor.getNom().trim());
        proveidor.setTelefon(netText(proveidor.getTelefon()));
        proveidor.setEmail(netText(proveidor.getEmail()));
        proveidor.setAdreca(netText(proveidor.getAdreca()));
        proveidor.setObservacions(netText(proveidor.getObservacions()));
    }

    private String normalitzarCif(String cif) {
        return cif == null ? null : cif.trim().toUpperCase().replace(" ", "");
    }

    private String netText(String text) {
        if (text == null) {
            return null;
        }

        String net = text.trim();
        return net.isEmpty() ? null : net;
    }

    @Override
    public List<Proveidor> filtrar(String filtre, String ordre) {
        List<Proveidor> proveidors;

        if (filtre != null && !filtre.isBlank()) {
            String text = filtre.trim();
            proveidors = proveidorRepository
                    .findByNomContainingIgnoreCaseOrCifContainingIgnoreCase(text, text);
        } else if ("nomAsc".equals(ordre)) {
            return proveidorRepository.findAllByOrderByNomAsc();
        } else if ("nomDesc".equals(ordre)) {
            return proveidorRepository.findAllByOrderByNomDesc();
        } else {
            proveidors = proveidorRepository.findAll();
        }

        if ("nomAsc".equals(ordre)) {
            proveidors.sort((a, b) -> a.getNom().compareToIgnoreCase(b.getNom()));
        } else if ("nomDesc".equals(ordre)) {
            proveidors.sort((a, b) -> b.getNom().compareToIgnoreCase(a.getNom()));
        }

        return proveidors;
    }

    @Override
    public void activar(Long id) {
        Proveidor proveidor = proveidorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveidor no trobat"));

        proveidor.setActiu(true);
        proveidorRepository.save(proveidor);
    }

}
