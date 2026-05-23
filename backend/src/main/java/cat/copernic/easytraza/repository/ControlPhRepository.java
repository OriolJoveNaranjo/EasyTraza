package cat.copernic.easytraza.repository;

import cat.copernic.easytraza.entities.ControlPh;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author orjon
 */
public interface ControlPhRepository extends JpaRepository<ControlPh, Long> {

    List<ControlPh> findAllByOrderByDataControlDesc();

    boolean existsByUsuariId(Long usuariId);

    
    boolean existsById(Long id);

    boolean existsByDataControlAfter(LocalDateTime data);
}
