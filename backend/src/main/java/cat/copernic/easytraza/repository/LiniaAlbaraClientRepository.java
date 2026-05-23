package cat.copernic.easytraza.repository;

import cat.copernic.easytraza.entities.LiniaAlbaraClient;
import cat.copernic.easytraza.enums.EstatAlbaraClient;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author orjon
 */
public interface LiniaAlbaraClientRepository extends JpaRepository<LiniaAlbaraClient, Long> {

    @Query("""
       SELECT DAY(l.albaraClient.data), SUM(l.quantitat)
       FROM LiniaAlbaraClient l
       WHERE l.albaraClient.data >= :inici
       AND l.albaraClient.data < :fi
       AND l.albaraClient.estat = :estat
       AND (:producteId IS NULL OR l.producte.id = :producteId)
       GROUP BY DAY(l.albaraClient.data)
       ORDER BY DAY(l.albaraClient.data)
       """)
    List<Object[]> vendesPerDia(
            @Param("inici") LocalDateTime inici,
            @Param("fi") LocalDateTime fi,
            @Param("producteId") Long producteId,
            @Param("estat") EstatAlbaraClient estat);

    boolean existsByProducteId(Long producteId);
}
