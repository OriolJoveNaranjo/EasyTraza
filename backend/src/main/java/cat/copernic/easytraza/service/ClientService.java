package cat.copernic.easytraza.service;

import cat.copernic.easytraza.entities.Client;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author orjon
 */
public interface ClientService {

    List<Client> findAll();

    Optional<Client> findById(Long id);

    Client save(Client client);

    Client update(Long id, Client client);

    String deleteById(Long id);

    List<Client> filtrar(String filtre, String ordre);

    void activar(Long id);
}
