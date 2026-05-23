package cat.copernic.easytraza.repository;

import cat.copernic.easytraza.entities.Usuari;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author orjon
 */
public interface UsuariRepository extends JpaRepository<Usuari, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);
    
    Optional<Usuari> findByEmail(String email);
}
