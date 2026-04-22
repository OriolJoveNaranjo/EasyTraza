package cat.copernic.easytraza.service.impl;

import cat.copernic.easytraza.entities.Client;
import cat.copernic.easytraza.repository.ClientRepository;
import cat.copernic.easytraza.service.ClientService;
import cat.copernic.easytraza.validation.NifValidator;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepo;

    public ClientServiceImpl(ClientRepository clientRepo) {
        this.clientRepo = clientRepo;
    }

    @Override
    public List<Client> findAll() {
        return clientRepo.findAll();
    }

    @Override
    public Optional<Client> findById(Long id) {
        return clientRepo.findById(id);
    }

    @Override
    @Transactional
    public Client save(Client client) {
        validarClient(client);

        String nifNet = normalitzarNif(client.getNif());
        client.setNif(nifNet);

        if (clientRepo.existsByNif(nifNet)) {
            throw new RuntimeException("Ja existeix un client amb aquest NIF");
        }

        return clientRepo.save(client);
    }

    @Override
    @Transactional
    public Client update(Long id, Client client) {
        Client existent = clientRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("El client no existeix"));

        validarClient(client);

        String nifNet = normalitzarNif(client.getNif());
        client.setNif(nifNet);

        if (clientRepo.existsByNifAndIdNot(nifNet, id)) {
            throw new RuntimeException("Ja existeix un client amb aquest NIF");
        }

        existent.setNif(nifNet);
        existent.setNom(netText(client.getNom()));
        existent.setCognoms(netText(client.getCognoms()));
        existent.setAdreca(netText(client.getAdreca()));
        existent.setRegistreSanitari(netText(client.getRegistreSanitari()));
        existent.setTelefon(netText(client.getTelefon()));
        existent.setEmail(netText(client.getEmail()));

        return clientRepo.save(existent);
    }

    @Override
    public void deleteById(Long id) {
        clientRepo.deleteById(id);
    }

    private void validarClient(Client client) {
        if (client.getNif() == null || client.getNif().trim().isEmpty()) {
            throw new RuntimeException("El NIF és obligatori");
        }

        String nifNet = normalitzarNif(client.getNif());
        client.setNif(nifNet);

        if (!NifValidator.validarDocument(nifNet)) {
            throw new RuntimeException("El NIF/CIF no és vàlid");
        }

        if (client.getNom() == null || client.getNom().trim().isEmpty()) {
            throw new RuntimeException("El nom és obligatori");
        }

        client.setNom(client.getNom().trim());
        client.setCognoms(netText(client.getCognoms()));
        client.setAdreca(netText(client.getAdreca()));
        client.setRegistreSanitari(netText(client.getRegistreSanitari()));
        client.setTelefon(netText(client.getTelefon()));
        client.setEmail(netText(client.getEmail()));
    }

    private String normalitzarNif(String nif) {
        return nif == null ? null : nif.trim().toUpperCase().replace(" ", "");
    }

    private String netText(String text) {
        if (text == null) {
            return null;
        }

        String net = text.trim();
        return net.isEmpty() ? null : net;
    }
}
