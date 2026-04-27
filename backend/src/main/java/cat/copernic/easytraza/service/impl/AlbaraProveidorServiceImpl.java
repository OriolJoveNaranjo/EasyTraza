/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.service.impl;

import cat.copernic.easytraza.entities.AlbaraProveidor;
import cat.copernic.easytraza.entities.LiniaAlbaraProveidor;
import cat.copernic.easytraza.entities.LotProveidor;
import cat.copernic.easytraza.entities.MateriaPrimera;
import cat.copernic.easytraza.entities.Proveidor;
import cat.copernic.easytraza.enums.EstatLot;
import cat.copernic.easytraza.repository.AlbaraProveidorRepository;
import cat.copernic.easytraza.repository.MateriaPrimeraRepository;
import cat.copernic.easytraza.repository.ProveidorRepository;
import cat.copernic.easytraza.repository.LotProveidorRepository;
import cat.copernic.easytraza.service.AlbaraProveidorService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author orjon
 */

@Service
public class AlbaraProveidorServiceImpl implements AlbaraProveidorService {

    private final AlbaraProveidorRepository albaraRepo;
    private final ProveidorRepository proveidorRepo;
    private final MateriaPrimeraRepository materiaRepo;
    private final LotProveidorRepository lotRepo;

    public AlbaraProveidorServiceImpl(
            AlbaraProveidorRepository albaraRepo,
            ProveidorRepository proveidorRepo,
            MateriaPrimeraRepository materiaRepo,
            LotProveidorRepository lotRepo
    ) {
        this.albaraRepo = albaraRepo;
        this.proveidorRepo = proveidorRepo;
        this.materiaRepo = materiaRepo;
        this.lotRepo = lotRepo;
    }

    @Override
    public List<AlbaraProveidor> findAll() {
        return albaraRepo.findAll();
    }

    @Override
    public Optional<AlbaraProveidor> findById(Long id) {
        return albaraRepo.findById(id);
    }

    /*@Transactional
    public AlbaraProveidor create(AlbaraProveidorRequest request) {
        validarRequest(request);

        Proveidor proveidor = proveidorRepo.findById(request.getProveidorId())
                .orElseThrow(() -> new RuntimeException("El proveïdor no existeix"));

        AlbaraProveidor albara = new AlbaraProveidor();
        albara.setDataRecepcio(request.getDataRecepcio());
        albara.setProveidor(proveidor);

        List<LiniaAlbaraProveidor> linies = new ArrayList<>();

        for (LiniaAlbaraProveidorRequest liniaReq : request.getLinies()) {
            MateriaPrimera materia = materiaRepo.findById(liniaReq.getMateriaPrimeraId())
                    .orElseThrow(() -> new RuntimeException("La matèria primera no existeix"));

            if (lotRepo.existsByIdentificadorLotAndProveidorId(
                    liniaReq.getIdentificadorLot(),
                    proveidor.getId()
            )) {
                throw new RuntimeException("Ja existeix un lot amb aquest identificador per aquest proveïdor");
            }

            LotProveidor lot = new LotProveidor();
            lot.setIdentificadorLot(liniaReq.getIdentificadorLot());
            lot.setProveidor(proveidor);
            lot.setMateriaPrimera(materia);
            lot.setQuantitat(liniaReq.getQuantitat());
            lot.setUnitat(liniaReq.getUnitat());
            lot.setDataCaducitat(liniaReq.getDataCaducitat());
            lot.setEstat(EstatLot.EN_ESTOC);
            lot.setAlbaraProveidor(albara);

            LiniaAlbaraProveidor linia = new LiniaAlbaraProveidor();
            linia.setAlbaraProveidor(albara);
            linia.setMateriaPrimera(materia);
            linia.setQuantitat(liniaReq.getQuantitat());
            linia.setUnitat(liniaReq.getUnitat());
            linia.setLot(lot);

            linies.add(linia);
        }

        albara.setLinies(linies);
        return albaraRepo.save(albara);
    }

    @Transactional
    public AlbaraProveidor update(Long id, AlbaraProveidorRequest request) {
        AlbaraProveidor existent = albaraRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("L'albarà no existeix"));

        validarRequest(request);

        Proveidor proveidor = proveidorRepo.findById(request.getProveidorId())
                .orElseThrow(() -> new RuntimeException("El proveïdor no existeix"));

        existent.setDataRecepcio(request.getDataRecepcio());
        existent.setProveidor(proveidor);

        existent.getLinies().clear();

        for (LiniaAlbaraProveidorRequest liniaReq : request.getLinies()) {
            MateriaPrimera materia = materiaRepo.findById(liniaReq.getMateriaPrimeraId())
                    .orElseThrow(() -> new RuntimeException("La matèria primera no existeix"));

            LotProveidor lot = new LotProveidor();
            lot.setIdentificadorLot(liniaReq.getIdentificadorLot());
            lot.setProveidor(proveidor);
            lot.setMateriaPrimera(materia);
            lot.setQuantitat(liniaReq.getQuantitat());
            lot.setUnitat(liniaReq.getUnitat());
            lot.setDataCaducitat(liniaReq.getDataCaducitat());
            lot.setEstat(EstatLot.EN_ESTOC);
            lot.setAlbaraProveidor(existent);

            LiniaAlbaraProveidor linia = new LiniaAlbaraProveidor();
            linia.setAlbaraProveidor(existent);
            linia.setMateriaPrimera(materia);
            linia.setQuantitat(liniaReq.getQuantitat());
            linia.setUnitat(liniaReq.getUnitat());
            linia.setLot(lot);

            existent.getLinies().add(linia);
        }

        return albaraRepo.save(existent);
    }*/
    @Override
    public void deleteById(Long id) {
        albaraRepo.deleteById(id);
    }

    /*private void validarRequest(AlbaraProveidorRequest request) {
        if (request.getProveidorId() == null) {
            throw new RuntimeException("El proveïdor és obligatori");
        }
        if (request.getDataRecepcio() == null) {
            throw new RuntimeException("La data de recepció és obligatòria");
        }
        if (request.getLinies() == null || request.getLinies().isEmpty()) {
            throw new RuntimeException("L'albarà ha de tenir almenys una línia");
        }

        for (LiniaAlbaraProveidorRequest linia : request.getLinies()) {
            if (linia.getMateriaPrimeraId() == null) {
                throw new RuntimeException("La matèria primera és obligatòria");
            }
            if (linia.getQuantitat() == null || linia.getQuantitat() <= 0) {
                throw new RuntimeException("La quantitat ha de ser superior a zero");
            }
            if (linia.getIdentificadorLot() == null || linia.getIdentificadorLot().isBlank()) {
                throw new RuntimeException("L'identificador del lot és obligatori");
            }
        }
    }*/
    @Override
    @Transactional
    public AlbaraProveidor save(AlbaraProveidor albaraProveidor) {
        if (albaraProveidor.getDataRecepcio() == null) {
            throw new RuntimeException("La data de recepció és obligatòria");
        }

        if (albaraProveidor.getDataRecepcio().isAfter(LocalDate.now())) {
            throw new RuntimeException("La data de recepció no pot ser futura");
        }

        albaraProveidor.setNumeroAlbara(albaraProveidor.getNumeroAlbara().trim());

        if (albaraRepo.existsByNumeroAlbara(albaraProveidor.getNumeroAlbara())) {
            throw new RuntimeException("Ja existeix un albarà amb aquest número");
        }

        if (albaraProveidor.getProveidor() == null || albaraProveidor.getProveidor().getId() == null) {
            throw new RuntimeException("El proveïdor és obligatori");
        }

        Proveidor proveidor = proveidorRepo.findById(albaraProveidor.getProveidor().getId())
                .orElseThrow(() -> new RuntimeException("El proveïdor no existeix"));

        albaraProveidor.setProveidor(proveidor);

        if (albaraProveidor.getLinies() == null || albaraProveidor.getLinies().isEmpty()) {
            throw new RuntimeException("L'albarà ha de tenir almenys una línia");
        }
        int liniesValides = 0;
        Set<String> lotsFormulari = new HashSet<>();
        for (LiniaAlbaraProveidor linia : albaraProveidor.getLinies()) {
            if (linia == null) {
                continue;
            }

            boolean teAlgunaDada
                    = linia.getMateriaPrimera() != null && linia.getMateriaPrimera().getId() != null
                    || linia.getQuantitat() != null
                    || linia.getUnitat() != null && !linia.getUnitat().trim().isEmpty()
                    || linia.getLot() != null && linia.getLot().getIdentificadorLot() != null && !linia.getLot().getIdentificadorLot().trim().isEmpty()
                    || linia.getLot() != null && linia.getLot().getDataCaducitat() != null;

            if (teAlgunaDada) {
                continue;
            }
                if (linia.getMateriaPrimera() == null || linia.getMateriaPrimera().getId() == null
                        || linia.getQuantitat() == null || linia.getQuantitat() <= 0
                        || linia.getUnitat() == null || linia.getUnitat().trim().isEmpty()
                        || linia.getLot() == null
                        || linia.getLot().getIdentificadorLot() == null || linia.getLot().getIdentificadorLot().trim().isEmpty()
                        || linia.getLot().getDataCaducitat() == null) {
                    throw new RuntimeException("Si afegeixes un lot, has d'omplir tots els camps del lot");
                }

                liniesValides++;
            
    

            if (linia.getMateriaPrimera() == null || linia.getMateriaPrimera().getId() == null) {
                throw new RuntimeException("La matèria primera és obligatòria");
            }

            MateriaPrimera materia = materiaRepo.findById(linia.getMateriaPrimera().getId())
                    .orElseThrow(() -> new RuntimeException("La matèria primera no existeix"));

            if (linia.getQuantitat() == null || linia.getQuantitat() <= 0) {
                throw new RuntimeException("La quantitat ha de ser superior a zero");
            }

            if (linia.getLot() == null) {
                throw new RuntimeException("El lot és obligatori");
            }

            if (linia.getLot().getIdentificadorLot() == null
                    || linia.getLot().getIdentificadorLot().trim().isEmpty()) {
                throw new RuntimeException("L'identificador del lot és obligatori");
            }

            if (lotRepo.existsByIdentificadorLotAndProveidorId(
                    linia.getLot().getIdentificadorLot().trim(),
                    proveidor.getId())) {
                throw new RuntimeException("Ja existeix un lot amb aquest identificador per aquest proveïdor");
            }
            if (linia.getLot().getDataCaducitat() == null) {
                throw new RuntimeException("La data de caducitat del lot és obligatòria");
            }

            if (linia.getLot().getDataCaducitat().isBefore(albaraProveidor.getDataRecepcio())) {
                throw new RuntimeException("La data de caducitat no pot ser anterior a la data de recepció");
            }
            String identificadorLotNet = linia.getLot().getIdentificadorLot().trim();

            if (!lotsFormulari.add(identificadorLotNet)) {
                throw new RuntimeException("No es poden repetir lots dins del mateix albarà");
            }

            if (lotRepo.existsByIdentificadorLotAndProveidorId(identificadorLotNet, proveidor.getId())) {
                throw new RuntimeException("Ja existeix un lot amb aquest identificador per aquest proveïdor");
            }

            linia.setAlbaraProveidor(albaraProveidor);
            linia.setMateriaPrimera(materia);

            LotProveidor lot = linia.getLot();
            lot.setIdentificadorLot(lot.getIdentificadorLot().trim());
            lot.setAlbaraProveidor(albaraProveidor);
            lot.setProveidor(proveidor);
            lot.setMateriaPrimera(materia);
            lot.setQuantitat(linia.getQuantitat());
            lot.setUnitat(linia.getUnitat());
            lot.setEstat(EstatLot.EN_ESTOC);
            lot.setIdentificadorLot(identificadorLotNet);
        }
        if (liniesValides == 0) {
            throw new RuntimeException("L'albarà ha de tenir almenys un lot complet");
        }

        return albaraRepo.save(albaraProveidor);
    }

    @Override
    @Transactional
    public AlbaraProveidor update(Long id, AlbaraProveidor albaraProveidor) {
        AlbaraProveidor existent = albaraRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("L'albarà no existeix"));

        String numeroAlbaraNet = null;

        if (albaraProveidor.getNumeroAlbara() != null && !albaraProveidor.getNumeroAlbara().trim().isEmpty()) {
            numeroAlbaraNet = albaraProveidor.getNumeroAlbara().trim();

            if (albaraRepo.existsByNumeroAlbaraAndIdNot(numeroAlbaraNet, id)) {
                throw new RuntimeException("Ja existeix un albarà amb aquest número");
            }
        }
        if (albaraProveidor.getDataRecepcio() == null) {
            throw new RuntimeException("La data de recepció és obligatòria");
        }

        if (albaraProveidor.getDataRecepcio().isAfter(LocalDate.now())) {
            throw new RuntimeException("La data de recepció no pot ser futura");
        }

        existent.setNumeroAlbara(numeroAlbaraNet);

        if (albaraProveidor.getDataRecepcio() != null) {
            existent.setDataRecepcio(albaraProveidor.getDataRecepcio());
        }

        if (albaraProveidor.getProveidor() == null || albaraProveidor.getProveidor().getId() == null) {
            throw new RuntimeException("El proveïdor és obligatori");
        }

        Proveidor proveidor = proveidorRepo.findById(albaraProveidor.getProveidor().getId())
                .orElseThrow(() -> new RuntimeException("El proveïdor no existeix"));

        existent.setProveidor(proveidor);
        existent.getLinies().clear();
        Set<String> lotsFormulari = new HashSet<>();
        int liniesValides = 0;
        if (albaraProveidor.getLinies() != null) {
            for (LiniaAlbaraProveidor liniaForm : albaraProveidor.getLinies()) {
                if (liniaForm == null) {
                    continue;
                }

                boolean teAlgunaDada
                        = (liniaForm.getMateriaPrimera() != null && liniaForm.getMateriaPrimera().getId() != null)
                        || liniaForm.getQuantitat() != null
                        || (liniaForm.getUnitat() != null && !liniaForm.getUnitat().trim().isEmpty())
                        || (liniaForm.getLot() != null && liniaForm.getLot().getIdentificadorLot() != null
                        && !liniaForm.getLot().getIdentificadorLot().trim().isEmpty())
                        || (liniaForm.getLot() != null && liniaForm.getLot().getDataCaducitat() != null);

                if (!teAlgunaDada) {
                    continue;
                }

                if (liniaForm.getMateriaPrimera() == null || liniaForm.getMateriaPrimera().getId() == null
                        || liniaForm.getQuantitat() == null || liniaForm.getQuantitat() <= 0
                        || liniaForm.getUnitat() == null || liniaForm.getUnitat().trim().isEmpty()
                        || liniaForm.getLot() == null
                        || liniaForm.getLot().getIdentificadorLot() == null
                        || liniaForm.getLot().getIdentificadorLot().trim().isEmpty()
                        || liniaForm.getLot().getDataCaducitat() == null) {
                    throw new RuntimeException("Si afegeixes un lot, has d'omplir tots els camps del lot");
                }

                liniesValides++;

                if (liniaForm.getLot() == null || liniaForm.getLot().getIdentificadorLot() == null
                        || liniaForm.getLot().getIdentificadorLot().trim().isEmpty()) {
                    continue;
                }
               
                String identificadorLotNet = liniaForm.getLot().getIdentificadorLot().trim();

                if (!lotsFormulari.add(identificadorLotNet)) {
                    throw new RuntimeException("No es poden repetir lots dins del mateix albarà");
                }

                if (lotRepo.existsByIdentificadorLotAndProveidorIdAndAlbaraProveidorIdNot(
                        identificadorLotNet,
                        proveidor.getId(),
                        id
                )) {
                    throw new RuntimeException("Ja existeix un lot amb aquest identificador per aquest proveïdor");
                }

                MateriaPrimera materia = materiaRepo.findById(liniaForm.getMateriaPrimera().getId())
                        .orElseThrow(() -> new RuntimeException("La matèria primera no existeix"));

                LiniaAlbaraProveidor linia = new LiniaAlbaraProveidor();
                linia.setAlbaraProveidor(existent);
                linia.setMateriaPrimera(materia);
                linia.setQuantitat(liniaForm.getQuantitat());
                linia.setUnitat(liniaForm.getUnitat());

                LotProveidor lot = new LotProveidor();
                lot.setIdentificadorLot(liniaForm.getLot().getIdentificadorLot().trim());
                lot.setDataCaducitat(liniaForm.getLot().getDataCaducitat());
                lot.setProveidor(proveidor);
                lot.setMateriaPrimera(materia);
                lot.setQuantitat(liniaForm.getQuantitat());
                lot.setUnitat(liniaForm.getUnitat());
                lot.setEstat(EstatLot.EN_ESTOC);
                lot.setAlbaraProveidor(existent);
                lot.setIdentificadorLot(identificadorLotNet);
                linia.setLot(lot);
                existent.getLinies().add(linia);
            }

        }
        if (liniesValides == 0) {
            throw new RuntimeException("L'albarà ha de tenir almenys un lot complet");
        }

        return albaraRepo.save(existent);
    }
}
