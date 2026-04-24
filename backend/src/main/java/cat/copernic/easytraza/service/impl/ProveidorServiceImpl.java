package cat.copernic.easytraza.service.impl;

import cat.copernic.easytraza.entities.Proveidor;
import cat.copernic.easytraza.repository.ProveidorRepository;
import cat.copernic.easytraza.service.ProveidorService;
import cat.copernic.easytraza.validation.CifValidator;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ProveidorServiceImpl implements ProveidorService {

    private final ProveidorRepository proveidorRepository;

    public ProveidorServiceImpl(ProveidorRepository proveidorRepository) {
        this.proveidorRepository = proveidorRepository;
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
            throw new RuntimeException("Ja existeix un proveïdor amb aquest CIF");
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
            throw new RuntimeException("Ja existeix un proveïdor amb aquest CIF");
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
    public void deleteById(Long id) {
        proveidorRepository.deleteById(id);
    }

    private void validarProveidor(Proveidor proveidor) {
        if (proveidor.getNom() == null || proveidor.getNom().trim().isEmpty()) {
            throw new RuntimeException("El nom és obligatori");
        }

        if (proveidor.getCif() == null || proveidor.getCif().trim().isEmpty()) {
            throw new RuntimeException("El CIF és obligatori");
        }

        String cifNet = normalitzarCif(proveidor.getCif());
        proveidor.setCif(cifNet);

        if (!CifValidator.validarCIF(cifNet)) {
            throw new IllegalArgumentException("El CIF no és vàlid");
        }

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
}
