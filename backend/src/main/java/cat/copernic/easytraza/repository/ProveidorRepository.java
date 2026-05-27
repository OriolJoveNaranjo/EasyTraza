package cat.copernic.easytraza.repository;

import cat.copernic.easytraza.entities.Proveidor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author orjon
 *
 * Repositori per gestionar proveïdors.
 *
 */
public interface ProveidorRepository extends JpaRepository<Proveidor, Long> {

    boolean existsByCif(String cif);

    boolean existsByCifAndIdNot(String cif, Long id);

    Optional<Proveidor> findByCif(String cif);

    List<Proveidor> findByNomContainingIgnoreCaseOrCifContainingIgnoreCase(String nom, String cif);

    List<Proveidor> findAllByOrderByNomAsc();

    List<Proveidor> findAllByOrderByNomDesc();
    
}
