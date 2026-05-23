package cat.copernic.easytraza.repository;

import cat.copernic.easytraza.entities.Tracabilitat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author orjon
 */
public interface TracabilitatRepository extends JpaRepository<Tracabilitat, Long> {

    List<Tracabilitat> findByLotProveidor_Id(Long lotId);

    List<Tracabilitat> findByLiniaAlbaraClient_Id(Long liniaId);

    List<Tracabilitat> findByLiniaAlbaraClient_AlbaraClient_Id(Long albaraId);

    void deleteByLiniaAlbaraClient_Id(Long liniaId);
    void deleteByLiniaAlbaraClient_AlbaraClient_Id(Long albaraId);
}
