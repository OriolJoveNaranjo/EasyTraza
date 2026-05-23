package cat.copernic.easytraza.service;

import cat.copernic.easytraza.entities.LotProveidor;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author orjon
 */
public interface LotProveidorService {

    List<LotProveidor> findAll();

    LotProveidor findById(Long id);

    void iniciarLot(Long lotId, boolean confirmarTancarAnterior);

    void finalitzarLot(Long lotId);

    List<LotProveidor> findByEstat(String estat);

    List<LotProveidor> filtrarLots(String identificador, String estat, Long materiaId, LocalDate data);

    void iniciarLotMobile(Long lotId, boolean confirmarTancarAnterior, Long usuariId);

}
