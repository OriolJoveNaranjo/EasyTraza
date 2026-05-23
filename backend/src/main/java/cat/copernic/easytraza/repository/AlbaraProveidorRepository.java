package cat.copernic.easytraza.repository;

import cat.copernic.easytraza.entities.AlbaraProveidor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author orjon
 */
public interface AlbaraProveidorRepository extends JpaRepository<AlbaraProveidor, Long> {

    boolean existsByNumeroAlbara(String numeroAlbara);

    boolean existsByNumeroAlbaraAndIdNot(String numeroAlbara, Long id);

    List<AlbaraProveidor> findByProveidorId(Long proveidorId);

    List<AlbaraProveidor> findAllByOrderByDataRecepcioAsc();

    List<AlbaraProveidor> findAllByOrderByDataRecepcioDesc();

    List<AlbaraProveidor> findByProveidorIdOrderByDataRecepcioAsc(Long proveidorId);

    List<AlbaraProveidor> findByProveidorIdOrderByDataRecepcioDesc(Long proveidorId);

    boolean existsByProveidorId(Long proveidorId);

    boolean existsByUsuariAltaId(Long usuariId);
}
