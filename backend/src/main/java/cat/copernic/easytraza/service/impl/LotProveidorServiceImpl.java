/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.service.impl;

import cat.copernic.easytraza.entities.LotProveidor;
import cat.copernic.easytraza.enums.EstatLot;
import cat.copernic.easytraza.repository.LotProveidorRepository;
import cat.copernic.easytraza.service.LotProveidorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 *
 * @author orjon
 */
@Service
public class LotProveidorServiceImpl implements LotProveidorService {

    private final LotProveidorRepository lotRepo;

    public LotProveidorServiceImpl(LotProveidorRepository lotRepo) {
        this.lotRepo = lotRepo;
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
    public void iniciarLot(Long id) {
        LotProveidor lot = findById(id);

        if (lot.getEstat() != EstatLot.EN_ESTOC) {
            throw new RuntimeException("Només es poden iniciar lots en estat EN_ESTOC");
        }

        lot.setEstat(EstatLot.OBERT);
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

        lotRepo.save(lot);
    }

}
