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
import java.util.Map;
import java.util.HashMap;
import cat.copernic.easytraza.entities.FitxerAlbaraProveidor;
import cat.copernic.easytraza.entities.Usuari;
import cat.copernic.easytraza.repository.UsuariRepository;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

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
    private final UsuariRepository usuarirepo;
    /**
     * Executa l'operació AlbaraProveidorServiceImpl.
     */

    public AlbaraProveidorServiceImpl(
            AlbaraProveidorRepository albaraRepo,
            ProveidorRepository proveidorRepo,
            MateriaPrimeraRepository materiaRepo,
            LotProveidorRepository lotRepo,
            UsuariRepository usuarirepo
    ) {
        this.albaraRepo = albaraRepo;
        this.proveidorRepo = proveidorRepo;
        this.materiaRepo = materiaRepo;
        this.lotRepo = lotRepo;
        this.usuarirepo = usuarirepo;
    }
    /**
     * Consulta dades i retorna la informació necessària per a la vista o l'API.
     */

    @Override
    public List<AlbaraProveidor> findAll() {
        return albaraRepo.findAll();
    }
    /**
     * Consulta dades i retorna la informació necessària per a la vista o l'API.
     */

    @Override
    public Optional<AlbaraProveidor> findById(Long id) {
        return albaraRepo.findById(id);
    }
    /**
     * Elimina o desactiva el registre indicat segons les regles de negoci.
     */

    @Override
    public void deleteById(Long id) {
        albaraRepo.deleteById(id);
    }
    /**
     * Valida i desa la informació rebuda.
     */

    @Override
    @Transactional
    public AlbaraProveidor save(AlbaraProveidor albaraProveidor) {
        if (albaraProveidor.getDataRecepcio() == null) {
            throw new RuntimeException("La data de recepció és obligatòria");
        }

        if (albaraProveidor.getDataRecepcio().isAfter(LocalDate.now())) {
            throw new RuntimeException("La data de recepció no pot ser futura");
        }
        if (albaraProveidor.getNumeroAlbara() == null || albaraProveidor.getNumeroAlbara().trim().isEmpty()) {
            throw new RuntimeException("El número d'albarà és obligatori");
        }
        if (albaraProveidor.getNumeroAlbara() == null || albaraProveidor.getNumeroAlbara().trim().isEmpty()) {
            throw new RuntimeException("El número d'albarà és obligatori");
        }

        String numero = albaraProveidor.getNumeroAlbara();

        if (numero != null && !numero.isBlank()) {
            numero = numero.trim();

            if (albaraRepo.existsByNumeroAlbara(numero)) {
                throw new RuntimeException("Aquest número d'albarà ja existeix");
            }

            albaraProveidor.setNumeroAlbara(numero);
        } else {
            albaraProveidor.setNumeroAlbara(null);
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

            if (!teAlgunaDada) {
                continue;
            }

            if (linia.getMateriaPrimera() == null || linia.getMateriaPrimera().getId() == null) {
                throw new RuntimeException("Has de seleccionar la matèria primera del lot");
            }

            if (linia.getQuantitat() == null || linia.getQuantitat() <= 0) {
                throw new RuntimeException("La quantitat del lot ha de ser superior a zero");
            }

            if (linia.getUnitat() == null || linia.getUnitat().trim().isEmpty()) {
                throw new RuntimeException("Has d'indicar la unitat del lot");
            }

            if (linia.getLot() == null) {
                throw new RuntimeException("El lot és obligatori");
            }

            if (linia.getLot().getIdentificadorLot() == null || linia.getLot().getIdentificadorLot().trim().isEmpty()) {
                throw new RuntimeException("Has d'indicar l'identificador del lot");
            }

            if (linia.getLot().getDataCaducitat() == null) {
                throw new RuntimeException("Has d'indicar la data de caducitat del lot");
            }

            liniesValides++;

            MateriaPrimera materia = materiaRepo.findById(linia.getMateriaPrimera().getId())
                    .orElseThrow(() -> new RuntimeException("La matèria primera no existeix"));

            if (linia.getLot().getDataCaducitat().isBefore(albaraProveidor.getDataRecepcio())) {
                throw new RuntimeException("La data de caducitat no pot ser anterior a la data de recepció");
            }

            String identificadorLotNet = linia.getLot().getIdentificadorLot().trim();

            String clauLot = identificadorLotNet + "|" + materia.getId();

            if (!lotsFormulari.add(clauLot)) {
                throw new RuntimeException("No es pot repetir el mateix lot amb la mateixa matèria primera dins del mateix albarà");
            }

            linia.setAlbaraProveidor(albaraProveidor);
            linia.setMateriaPrimera(materia);

            LotProveidor lot = linia.getLot();
            lot.setIdentificadorLot(identificadorLotNet);
            lot.setAlbaraProveidor(albaraProveidor);
            lot.setProveidor(proveidor);
            lot.setMateriaPrimera(materia);
            lot.setQuantitat(linia.getQuantitat());
            lot.setUnitat(linia.getUnitat().trim());
            lot.setEstat(EstatLot.EN_ESTOC);
        }
        if (albaraProveidor.getUsuariAlta() == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();

            Usuari usuari = usuarirepo.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuari no trobat"));

            albaraProveidor.setUsuariAlta(usuari);
        }

        if (liniesValides == 0) {
            throw new RuntimeException("L'albarà ha de tenir almenys un lot complet");
        }

        return albaraRepo.save(albaraProveidor);
    }
    /**
     * Actualitza una entitat existent amb les dades indicades.
     */

    @Override
    @Transactional
    public AlbaraProveidor update(Long id, AlbaraProveidor albaraProveidor) {
        AlbaraProveidor existent = albaraRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("L'albarà no existeix"));

        if (albaraProveidor.getDataRecepcio() == null) {
            throw new RuntimeException("La data de recepció és obligatòria");
        }

        if (albaraProveidor.getDataRecepcio().isAfter(LocalDate.now())) {
            throw new RuntimeException("La data de recepció no pot ser futura");
        }

        String numeroAlbaraNet = null;

        if (albaraProveidor.getNumeroAlbara() != null && !albaraProveidor.getNumeroAlbara().trim().isEmpty()) {
            numeroAlbaraNet = albaraProveidor.getNumeroAlbara().trim();

            if (albaraRepo.existsByNumeroAlbaraAndIdNot(numeroAlbaraNet, id)) {
                throw new RuntimeException("Aquest número d'albarà ja existeix");
            }
        }

        if (albaraProveidor.getProveidor() == null || albaraProveidor.getProveidor().getId() == null) {
            throw new RuntimeException("El proveïdor és obligatori");
        }

        Proveidor proveidor = proveidorRepo.findById(albaraProveidor.getProveidor().getId())
                .orElseThrow(() -> new RuntimeException("El proveïdor no existeix"));

        existent.setNumeroAlbara(numeroAlbaraNet);
        existent.setDataRecepcio(albaraProveidor.getDataRecepcio());
        existent.setProveidor(proveidor);

        Map<String, EstatLot> estatsAnteriors = new HashMap<>();

        for (LiniaAlbaraProveidor liniaExistent : existent.getLinies()) {
            if (liniaExistent.getLot() != null
                    && liniaExistent.getLot().getIdentificadorLot() != null
                    && liniaExistent.getLot().getEstat() != null) {

                String lotKey = liniaExistent.getLot().getIdentificadorLot().trim();
                estatsAnteriors.put(lotKey, liniaExistent.getLot().getEstat());
            }
        }

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

                if (liniaForm.getMateriaPrimera() == null || liniaForm.getMateriaPrimera().getId() == null) {
                    throw new RuntimeException("Has de seleccionar la matèria primera del lot");
                }

                if (liniaForm.getQuantitat() == null || liniaForm.getQuantitat() <= 0) {
                    throw new RuntimeException("La quantitat del lot ha de ser superior a zero");
                }

                if (liniaForm.getUnitat() == null || liniaForm.getUnitat().trim().isEmpty()) {
                    throw new RuntimeException("Has d'indicar la unitat del lot");
                }

                if (liniaForm.getLot() == null) {
                    throw new RuntimeException("El lot és obligatori");
                }

                if (liniaForm.getLot().getIdentificadorLot() == null
                        || liniaForm.getLot().getIdentificadorLot().trim().isEmpty()) {
                    throw new RuntimeException("Has d'indicar l'identificador del lot");
                }

                if (liniaForm.getLot().getDataCaducitat() == null) {
                    throw new RuntimeException("Has d'indicar la data de caducitat del lot");
                }

                liniesValides++;

                String identificadorLotNet = liniaForm.getLot().getIdentificadorLot().trim();

                String clauLot = identificadorLotNet + "|" + liniaForm.getMateriaPrimera().getId();

                if (!lotsFormulari.add(clauLot)) {
                    throw new RuntimeException("No es pot repetir el mateix lot amb la mateixa matèria primera dins del mateix albarà");
                }

                if (liniaForm.getLot().getDataCaducitat().isBefore(albaraProveidor.getDataRecepcio())) {
                    throw new RuntimeException("La data de caducitat no pot ser anterior a la data de recepció");
                }

                MateriaPrimera materia = materiaRepo.findById(liniaForm.getMateriaPrimera().getId())
                        .orElseThrow(() -> new RuntimeException("La matèria primera no existeix"));

                LiniaAlbaraProveidor linia = new LiniaAlbaraProveidor();
                linia.setAlbaraProveidor(existent);
                linia.setMateriaPrimera(materia);
                linia.setQuantitat(liniaForm.getQuantitat());
                linia.setUnitat(liniaForm.getUnitat().trim());

                LotProveidor lot = new LotProveidor();

                EstatLot estatAnterior = estatsAnteriors.getOrDefault(
                        identificadorLotNet,
                        EstatLot.EN_ESTOC
                );

                lot.setEstat(estatAnterior);
                lot.setIdentificadorLot(identificadorLotNet);
                lot.setDataCaducitat(liniaForm.getLot().getDataCaducitat());
                lot.setProveidor(proveidor);
                lot.setMateriaPrimera(materia);
                lot.setQuantitat(liniaForm.getQuantitat());
                lot.setUnitat(liniaForm.getUnitat().trim());
                lot.setAlbaraProveidor(existent);

                linia.setLot(lot);
                existent.getLinies().add(linia);
            }
        }

        if (liniesValides == 0) {
            throw new RuntimeException("L'albarà ha de tenir almenys un lot complet");
        }

        return albaraRepo.save(existent);
    }
    /**
     * Valida i desa la informació rebuda.
     */

    @Override
    @Transactional
    public AlbaraProveidor save(AlbaraProveidor albaraProveidor, MultipartFile[] fitxers) {
        afegirFitxers(albaraProveidor, fitxers);
        return save(albaraProveidor);
    }
    /**
     * Actualitza una entitat existent amb les dades indicades.
     */

    @Override
    @Transactional
    public AlbaraProveidor update(Long id, AlbaraProveidor albaraProveidor, MultipartFile[] fitxers) {
        AlbaraProveidor actualitzat = update(id, albaraProveidor);
        afegirFitxers(actualitzat, fitxers);
        return albaraRepo.save(actualitzat);
    }

    private void prepararLinies(AlbaraProveidor albaraProveidor) {
        if (albaraProveidor.getLinies() != null) {
            albaraProveidor.getLinies().forEach(linia -> linia.setAlbaraProveidor(albaraProveidor));
        }
    }

    private void afegirFitxers(AlbaraProveidor albaraProveidor, MultipartFile[] fitxers) {
        if (fitxers == null) {
            return;
        }

        for (MultipartFile file : fitxers) {
            if (file != null && !file.isEmpty()) {
                try {
                    FitxerAlbaraProveidor fitxer = new FitxerAlbaraProveidor();
                    fitxer.setNomFitxer(file.getOriginalFilename());
                    fitxer.setTipusFitxer(file.getContentType());
                    fitxer.setDades(file.getBytes());
                    fitxer.setAlbaraProveidor(albaraProveidor);

                    albaraProveidor.getFitxers().add(fitxer);
                } catch (IOException e) {
                    throw new RuntimeException("No s'ha pogut guardar el fitxer: " + file.getOriginalFilename());
                }
            }
        }
    }
    /**
     * Executa l'operació filtrar.
     */

    @Override
    public List<AlbaraProveidor> filtrar(Long proveidorId, String ordre) {

        if (proveidorId != null && "dataAsc".equals(ordre)) {
            return albaraRepo.findByProveidorIdOrderByDataRecepcioAsc(proveidorId);
        }

        if (proveidorId != null && "dataDesc".equals(ordre)) {
            return albaraRepo.findByProveidorIdOrderByDataRecepcioDesc(proveidorId);
        }

        if (proveidorId != null) {
            return albaraRepo.findByProveidorId(proveidorId);
        }

        if ("dataAsc".equals(ordre)) {
            return albaraRepo.findAllByOrderByDataRecepcioAsc();
        }

        if ("dataDesc".equals(ordre)) {
            return albaraRepo.findAllByOrderByDataRecepcioDesc();
        }

        return albaraRepo.findAll();
    }
}
