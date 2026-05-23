package cat.copernic.easytraza.service;

import cat.copernic.easytraza.entities.Usuari;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author orjon
 *
 * Servei de gestió d'usuaris.
 */
public interface UsuariService {

    List<Usuari> findAll();

    Optional<Usuari> findById(Long id);

    Usuari save(Usuari usuari);

    Usuari update(Long id, Usuari usuari);

    String deleteById(Long id);

    void activar(Long id);
}
