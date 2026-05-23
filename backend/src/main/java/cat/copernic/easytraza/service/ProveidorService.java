package cat.copernic.easytraza.service;

import cat.copernic.easytraza.entities.Proveidor;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author orjon
 *
 * Servei de gestió de proveïdors.
 *
 */
public interface ProveidorService {

    List<Proveidor> findAll();

    Optional<Proveidor> findById(Long id);

    Proveidor save(Proveidor proveidor);

    Proveidor update(Long id, Proveidor proveidor);

    String deleteById(Long id);

    List<Proveidor> filtrar(String filtre, String ordre);

    void activar(Long id);
}
