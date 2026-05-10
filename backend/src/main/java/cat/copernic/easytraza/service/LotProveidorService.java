/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.service;

import cat.copernic.easytraza.entities.LotProveidor;
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

}
