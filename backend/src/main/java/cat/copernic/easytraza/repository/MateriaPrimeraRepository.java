package cat.copernic.easytraza.repository;

import cat.copernic.easytraza.entities.MateriaPrimera;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author orjon Repositori per gestionar matèries primeres.
 */
public interface MateriaPrimeraRepository extends JpaRepository<MateriaPrimera, Long> {

    boolean existsByNom(String nom);

    boolean existsByNomAndIdNot(String nom, Long id);

}
