package cat.copernic.easytraza.service.impl;

import cat.copernic.easytraza.entities.Client;
import cat.copernic.easytraza.enums.EstatAlbaraClient;
import cat.copernic.easytraza.repository.AlbaraClientRepository;
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
    private final AlbaraClientRepository albaraCliRepo;

    public ClientServiceImpl(ClientRepository clientRepo, AlbaraClientRepository albaraCliRepo) {
        this.clientRepo = clientRepo;
        this.albaraCliRepo = albaraCliRepo;
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

        String emailNet = netText(client.getEmail());
        String nifNet = normalitzarNif(client.getNif());
        client.setNif(nifNet);

        if (clientRepo.existsByNif(nifNet)) {
            throw new RuntimeException("Ja existeix un client amb aquest NIF");
        }
        if (emailNet != null && clientRepo.existsByEmail(emailNet)) {
            throw new RuntimeException("Aquest correu ja existeix a la base de dades, no es pot repetir");
        }
        client.setEmail(emailNet);
        return clientRepo.save(client);
    }

    @Override
    @Transactional
    public Client update(Long id, Client client) {
        Client existent = clientRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("El client no existeix"));

        validarClient(client);

        String emailNet = netText(client.getEmail());
        String nifNet = normalitzarNif(client.getNif());
        client.setNif(nifNet);

        if (clientRepo.existsByNifAndIdNot(nifNet, id)) {
            throw new RuntimeException("Ja existeix un client amb aquest NIF");
        }
        if (emailNet != null && clientRepo.existsByEmailAndIdNot(emailNet, id)) {
            throw new RuntimeException("Aquest correu ja existeix a la base de dades, no es pot repetir");
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
    public String deleteById(Long id) {
        Client client = clientRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Client no trobat"));

        boolean teAlbaransPendents = albaraCliRepo
                .existsByClientIdAndEstat(id, EstatAlbaraClient.PENDENT);

        if (teAlbaransPendents) {
            throw new RuntimeException("No es pot eliminar el client perquè té albarans pendents.");
        }

        boolean teAlbarans = albaraCliRepo.existsByClientId(id);

        if (teAlbarans) {
            client.setActiu(false);
            clientRepo.save(client);
            return "Aquest client no es pot eliminar perquè té dades associades, però s'ha desactivat.";
        }

        clientRepo.deleteById(id);
        return "Client eliminat correctament.";
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

    @Override
    public List<Client> filtrar(String filtre, String ordre) {
        List<Client> clients;

        if (filtre != null && !filtre.isBlank()) {
            String text = filtre.trim();
            clients = clientRepo.findByNomContainingIgnoreCaseOrNifContainingIgnoreCase(text, text);
        } else if ("nomAsc".equals(ordre)) {
            return clientRepo.findAllByOrderByNomAsc();
        } else if ("nomDesc".equals(ordre)) {
            return clientRepo.findAllByOrderByNomDesc();
        } else {
            clients = clientRepo.findAll();
        }

        if ("nomAsc".equals(ordre)) {
            clients.sort((a, b) -> a.getNom().compareToIgnoreCase(b.getNom()));
        } else if ("nomDesc".equals(ordre)) {
            clients.sort((a, b) -> b.getNom().compareToIgnoreCase(a.getNom()));
        }

        return clients;
    }

    public void activar(Long id) {
        Client client = clientRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Client no trobat"));

        client.setActiu(true);
        clientRepo.save(client);
    }
}
