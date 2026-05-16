/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.service.impl;

import cat.copernic.easytraza.entities.LotProveidor;
import cat.copernic.easytraza.entities.Usuari;
import cat.copernic.easytraza.enums.EstatLot;
import cat.copernic.easytraza.repository.LotProveidorRepository;
import cat.copernic.easytraza.repository.UsuariRepository;
import cat.copernic.easytraza.service.LotProveidorService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author orjon
 */
@Service
public class LotProveidorServiceImpl implements LotProveidorService {

    private final LotProveidorRepository lotRepo;
    private final UsuariRepository usuarirepo;

    public LotProveidorServiceImpl(LotProveidorRepository lotRepo, UsuariRepository usuarirepo) {
        this.lotRepo = lotRepo;
        this.usuarirepo = usuarirepo;
    }

    @Override
    public List<LotProveidor> findAll() {
        return lotRepo.findAll();
    }

    @Override
    public LotProveidor findById(Long id) {
        return lotRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("El lot no existeix"));
    }

    @Override
    @Transactional
    public void iniciarLot(Long lotId, boolean confirmarTancarAnterior) {
        LotProveidor lot = lotRepo.findById(lotId)
                .orElseThrow(() -> new RuntimeException("El lot no existeix"));

        if (lot.getMateriaPrimera() == null || lot.getMateriaPrimera().getId() == null) {
            throw new RuntimeException("El lot no té matèria primera associada");
        }

        Optional<LotProveidor> lotObertAnterior = lotRepo.findByMateriaPrimeraIdAndEstat(
                lot.getMateriaPrimera().getId(),
                EstatLot.OBERT
        );

        if (lotObertAnterior.isPresent()
                && !lotObertAnterior.get().getId().equals(lot.getId())
                && !confirmarTancarAnterior) {
            throw new RuntimeException("Ja hi ha un lot obert d'aquesta matèria primera. Cal confirmar que vols finalitzar-lo.");
        }

        if (lotObertAnterior.isPresent()
                && !lotObertAnterior.get().getId().equals(lot.getId())) {
            LotProveidor anterior = lotObertAnterior.get();
            anterior.setEstat(EstatLot.ACABAT);
            anterior.setDataAcabament(LocalDateTime.now());
            lotRepo.save(anterior);
        }

        lot.setEstat(EstatLot.OBERT);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Usuari usuari = usuarirepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuari no trobat"));
        lot.setUsuariObertura(usuari);
        lot.setDataObertura(LocalDateTime.now());

        lotRepo.save(lot);
    }

    @Override
    public List<LotProveidor> findByEstat(String estat) {
        if (estat == null || estat.isBlank()) {
            return lotRepo.findAll();
        }

        return lotRepo.findByEstat(EstatLot.valueOf(estat));
    }

    @Transactional
    @Override
    public void finalitzarLot(Long id) {
        LotProveidor lot = lotRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("El lot no existeix"));

        if (lot.getEstat() != EstatLot.OBERT) {
            throw new RuntimeException("Només es pot finalitzar un lot que està OBERT");
        }

        lot.setEstat(EstatLot.ACABAT);
        lot.setDataAcabament(LocalDateTime.now());

        lotRepo.save(lot);
    }

    @Override
    public List<LotProveidor> filtrarLots(String identificador, String estat, Long materiaId, LocalDate data) {

        EstatLot estatEnum = null;

        if (estat != null && !estat.isBlank()) {
            estatEnum = EstatLot.valueOf(estat);
        }

        return lotRepo.filtrarLots(
                identificador != null && !identificador.isBlank() ? identificador : null,
                estatEnum,
                materiaId,
                data
        );
    }

}
