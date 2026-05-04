/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.service.impl;

import cat.copernic.easytraza.entities.AlbaraClient;
import cat.copernic.easytraza.enums.EstatAlbaraClient;
import cat.copernic.easytraza.repository.AlbaraClientRepository;
import cat.copernic.easytraza.service.AlbaraClientService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 *
 * @author orjon
 */
@Service
public class AlbaraClientServiceImpl implements AlbaraClientService {

    private final AlbaraClientRepository repository;

    public AlbaraClientServiceImpl(AlbaraClientRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<AlbaraClient> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<AlbaraClient> findById(Long id) {
        return repository.findById(id);
    }

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

    @Override
    public void deleteById(Long id) {
        AlbaraClient existent = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Albarà de client no trobat"));

        if (existent.getEstat() == EstatAlbaraClient.LLIURAT) {
            throw new RuntimeException("No es pot eliminar un albarà lliurat");
        }

        repository.deleteById(id);
    }

    @Override
    public void marcarComLliurat(Long id) {
        AlbaraClient albara = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Albarà de client no trobat"));

        albara.setEstat(EstatAlbaraClient.LLIURAT);
        repository.save(albara);
    }

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
}
