package cat.copernic.easytraza.service.impl;

import cat.copernic.easytraza.entities.MateriaPrimera;
import cat.copernic.easytraza.repository.LotProveidorRepository;
import cat.copernic.easytraza.repository.MateriaPrimeraRepository;
import cat.copernic.easytraza.service.MateriaPrimeraService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author orjon
 */
@Service
public class MateriaPrimeraServiceImpl implements MateriaPrimeraService {

    private final MateriaPrimeraRepository materiaPrimeraRepository; 
    private final LotProveidorRepository lotProvRepo;
    /**
     * Executa l'operació MateriaPrimeraServiceImpl.
     */

    public MateriaPrimeraServiceImpl(MateriaPrimeraRepository materiaPrimeraRepository,LotProveidorRepository lotProvRepo) {
        this.materiaPrimeraRepository = materiaPrimeraRepository;
        this.lotProvRepo = lotProvRepo;
    }
    /**
     * Consulta dades i retorna la informació necessària per a la vista o l'API.
     */

    @Override
    public List<MateriaPrimera> findAll() {
        return materiaPrimeraRepository.findAll();
    }
    /**
     * Consulta dades i retorna la informació necessària per a la vista o l'API.
     */

    @Override
    public Optional<MateriaPrimera> findById(Long id) {
        return materiaPrimeraRepository.findById(id);
    }
    /**
     * Valida i desa la informació rebuda.
     */

    @Override
    public MateriaPrimera save(MateriaPrimera materiaPrimera) {
        if (materiaPrimeraRepository.existsByNom(materiaPrimera.getNom())) {
            throw new RuntimeException("Ja existeix una matèria primera amb aquest nom");
        }

        return materiaPrimeraRepository.save(materiaPrimera);
    }
    /**
     * Actualitza una entitat existent amb les dades indicades.
     */

    @Override
    public MateriaPrimera update(Long id, MateriaPrimera materiaPrimera) {
        Optional<MateriaPrimera> existent = materiaPrimeraRepository.findById(id);

        if (existent.isEmpty()) {
            throw new RuntimeException("La matèria primera no existeix");
        }

        if (materiaPrimeraRepository.existsByNomAndIdNot(materiaPrimera.getNom(), id)) {
            throw new RuntimeException("Ja existeix una matèria primera amb aquest nom");
        }

        MateriaPrimera actual = existent.get();
        actual.setNom(materiaPrimera.getNom());
        actual.setDescripcio(materiaPrimera.getDescripcio());

        return materiaPrimeraRepository.save(actual);
    }
    /**
     * Elimina o desactiva el registre indicat segons les regles de negoci.
     */

    @Override
    public String deleteById(Long id) {
        MateriaPrimera materia = materiaPrimeraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matèria primera no trobada"));

        boolean teLots = lotProvRepo.existsByMateriaPrimeraId(id);

        if (teLots) {
            materia.setActiu(false);
            materiaPrimeraRepository.save(materia);
            return "Aquesta matèria primera té dades associades i s'ha desactivat.";
        }

        materiaPrimeraRepository.deleteById(id);
        return "Matèria primera eliminada correctament.";
    }
    /**
     * Activa el registre indicat.
     */

    public void activar(Long id) {
        MateriaPrimera materia = materiaPrimeraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matèria primera no trobada"));

        materia.setActiu(true);
        materiaPrimeraRepository.save(materia);
    }
}
