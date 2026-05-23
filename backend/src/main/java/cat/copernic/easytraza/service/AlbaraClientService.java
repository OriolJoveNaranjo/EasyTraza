package cat.copernic.easytraza.service;

import cat.copernic.easytraza.entities.AlbaraClient;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author orjon
 */
public interface AlbaraClientService {

    List<AlbaraClient> findAll();

    Optional<AlbaraClient> findById(Long id);

    AlbaraClient save(AlbaraClient albara);

    AlbaraClient update(Long id, AlbaraClient albara);

    void deleteById(Long id);

    void marcarComLliurat(Long id);

    List<AlbaraClient> filtrar(Long clientId, String estat, String ordre);

    AlbaraClient saveAmbTracabilitatAutomatica(AlbaraClient albara);
}
