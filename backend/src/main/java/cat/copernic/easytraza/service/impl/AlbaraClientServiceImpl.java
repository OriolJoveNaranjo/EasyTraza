package cat.copernic.easytraza.service.impl;

import cat.copernic.easytraza.entities.AlbaraClient;
import cat.copernic.easytraza.enums.EstatAlbaraClient;
import cat.copernic.easytraza.repository.AlbaraClientRepository;
import cat.copernic.easytraza.service.AlbaraClientService;
import java.util.Optional;
import org.springframework.stereotype.Service;
import cat.copernic.easytraza.entities.LiniaAlbaraClient;
import cat.copernic.easytraza.entities.LotProveidor;
import cat.copernic.easytraza.entities.Tracabilitat;
import cat.copernic.easytraza.enums.EstatLot;
import cat.copernic.easytraza.repository.LotProveidorRepository;
import cat.copernic.easytraza.repository.TracabilitatRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author orjon
 */
@Service
public class AlbaraClientServiceImpl implements AlbaraClientService {

    private final AlbaraClientRepository repository;
    private final LotProveidorRepository lotProveidorRepository;
    private final TracabilitatRepository tracabilitatRepository;
    /**
     * Executa l'operació AlbaraClientServiceImpl.
     * @param repository
     * @param lotProveidorRepository
     * @param tracabilitatRepository
     */

    public AlbaraClientServiceImpl(AlbaraClientRepository repository, LotProveidorRepository lotProveidorRepository,
            TracabilitatRepository tracabilitatRepository) {
        this.repository = repository;
        this.lotProveidorRepository = lotProveidorRepository;
        this.tracabilitatRepository = tracabilitatRepository;
    }
    /**
     * Consulta dades i retorna la informació necessària per a la vista o l'API.
     * @return 
     */

    @Override
    public List<AlbaraClient> findAll() {
        return repository.findAll();
    }
    /**
     * Consulta dades i retorna la informació necessària per a la vista o l'API.
     * @param id
     * @return 
     */

    @Override
    public Optional<AlbaraClient> findById(Long id) {
        return repository.findById(id);
    }
    /**
     * Valida i desa la informació rebuda.
     * @param albara
     * @return 
     */

    @Override
    public AlbaraClient save(AlbaraClient albara) {
        if (albara.getEstat() == null) {
            albara.setEstat(EstatAlbaraClient.PENDENT);
        }

        if (albara.getData() == null) {
            albara.setData(LocalDateTime.now());
        }

        if (albara.getLinies() != null) {
            albara.getLinies().forEach(linia -> linia.setAlbaraClient(albara));
        }

        return repository.save(albara);
    }
    /**
     * Actualitza una entitat existent amb les dades indicades.
     * @param id
     * @param albara
     * @return 
     */

    @Override
    public AlbaraClient update(Long id, AlbaraClient albara) {
        AlbaraClient existent = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Albarà de client no trobat"));

        if (existent.getEstat() == EstatAlbaraClient.LLIURAT) {
            throw new RuntimeException("No es pot modificar un albarà lliurat");
        }

        albara.setId(id);

        if (albara.getEstat() == null) {
            albara.setEstat(existent.getEstat());
        }

        if (albara.getLinies() != null) {
            albara.getLinies().forEach(linia -> linia.setAlbaraClient(albara));
        }

        return repository.save(albara);
    }
    /**
     * Elimina o desactiva el registre indicat segons les regles de negoci.
     * @param id
     */

    @Override
    @Transactional
    public void deleteById(Long id) {
        AlbaraClient existent = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Albarà de client no trobat"));

        if (existent.getEstat() == EstatAlbaraClient.LLIURAT) {
            throw new RuntimeException("No es pot eliminar un albarà lliurat");
        }

        tracabilitatRepository.deleteByLiniaAlbaraClient_AlbaraClient_Id(id);

        repository.deleteById(id);
    }
    /**
     * Executa l'operació marcarComLliurat.
     * @param id
     */

    @Override
    public void marcarComLliurat(Long id) {
        AlbaraClient albara = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Albarà de client no trobat"));

        albara.setEstat(EstatAlbaraClient.LLIURAT);
        repository.save(albara);
    }
    /**
     * Executa l'operació filtrar.
     * @param clientId
     * @param estat
     * @param ordre
     * @return 
     */

    @Override
    public List<AlbaraClient> filtrar(Long clientId, String estat, String ordre) {
        EstatAlbaraClient estatEnum = null;

        if (estat != null && !estat.isBlank()) {
            estatEnum = EstatAlbaraClient.valueOf(estat);
        }

        List<AlbaraClient> albarans;

        if (clientId != null && estatEnum != null) {
            albarans = repository.findByClientIdAndEstat(clientId, estatEnum);
        } else if (clientId != null) {
            albarans = repository.findByClientId(clientId);
        } else if (estatEnum != null) {
            albarans = repository.findByEstat(estatEnum);
        } else if ("dataAsc".equals(ordre)) {
            return repository.findAllByOrderByDataAsc();
        } else if ("dataDesc".equals(ordre)) {
            return repository.findAllByOrderByDataDesc();
        } else {
            albarans = repository.findAll();
        }

        if ("dataAsc".equals(ordre)) {
            albarans.sort((a, b) -> a.getData().compareTo(b.getData()));
        } else if ("dataDesc".equals(ordre)) {
            albarans.sort((a, b) -> b.getData().compareTo(a.getData()));
        }

        return albarans;
    }
    /**
     * Valida i desa la informació rebuda.
     * @param albara
     * @return 
     */

    @Override
    @Transactional
    public AlbaraClient saveAmbTracabilitatAutomatica(AlbaraClient albara) {

        if (albara.getLinies() != null) {
            albara.setLinies(
                    albara.getLinies().stream()
                            .filter(linia -> linia.getProducte() != null && linia.getProducte().getId() != null)
                            .toList()
            );
        }

        if (albara.getEstat() == null) {
            albara.setEstat(EstatAlbaraClient.PENDENT);
        }

        if (albara.getData() == null) {
            albara.setData(LocalDateTime.now());
        }

        if (albara.getLinies() != null) {
            for (LiniaAlbaraClient linia : albara.getLinies()) {
                linia.setAlbaraClient(albara);
            }
        }

        AlbaraClient albaraGuardat = repository.save(albara);

        if (albaraGuardat.getLinies() != null) {
            for (LiniaAlbaraClient linia : albaraGuardat.getLinies()) {
                tracabilitatRepository.deleteByLiniaAlbaraClient_Id(linia.getId());
            }

            List<LotProveidor> lotsOberts = lotProveidorRepository.findByEstat(EstatLot.OBERT);

            for (LiniaAlbaraClient linia : albaraGuardat.getLinies()) {

                if (linia.getProducte() == null || linia.getProducte().getId() == null) {
                    continue;
                }

                for (LotProveidor lot : lotsOberts) {
                    Tracabilitat t = new Tracabilitat();
                    t.setLiniaAlbaraClient(linia);
                    t.setLotProveidor(lot);
                    t.setProducteFinal(linia.getProducte());
                    t.setDataRegistre(LocalDateTime.now());

                    tracabilitatRepository.save(t);
                }
            }
        }

        return albaraGuardat;
    }
}
