/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.repository;

import cat.copernic.easytraza.entities.LotProveidor;
import cat.copernic.easytraza.enums.EstatLot;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author orjon
 */
public interface LotProveidorRepository extends JpaRepository<LotProveidor, Long> {

    boolean existsByIdentificadorLotAndProveidorId(String identificadorLot, Long proveidorId);

    Optional<LotProveidor> findByIdentificadorLotAndProveidorId(String identificadorLot, Long proveidorId);

    boolean existsByIdentificadorLotAndProveidorIdAndAlbaraProveidorIdNot(
            String identificadorLot,
            Long proveidorId,
            Long albaraId);

    List<LotProveidor> findByEstat(EstatLot estat);

    List<LotProveidor> findByEstatNot(EstatLot estat);

    Optional<LotProveidor> findByMateriaPrimeraIdAndEstat(Long materiaPrimeraId, EstatLot estat);

    @Query("""
    SELECT l FROM LotProveidor l
    WHERE (:identificador IS NULL OR LOWER(l.identificadorLot) LIKE LOWER(CONCAT('%', :identificador, '%')))
    AND (:estat IS NULL OR l.estat = :estat)
    AND (:materiaId IS NULL OR l.materiaPrimera.id = :materiaId)
    AND (:data IS NULL OR l.dataCaducitat = :data)
    """)
    List<LotProveidor> filtrarLots(
            @Param("identificador") String identificador,
            @Param("estat") EstatLot estat,
            @Param("materiaId") Long materiaId,
            @Param("data") LocalDate data);

    boolean existsByMateriaPrimeraId(Long materiaPrimeraId);

    boolean existsByUsuariOberturaId(Long usuariId);

    long countByEstat(EstatLot estat);

    long countByDataCaducitatBetween(LocalDate dataInici, LocalDate dataFi);
}
