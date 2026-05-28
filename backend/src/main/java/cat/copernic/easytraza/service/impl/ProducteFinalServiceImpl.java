package cat.copernic.easytraza.service.impl;

import cat.copernic.easytraza.entities.ProducteFinal;
import cat.copernic.easytraza.repository.LiniaAlbaraClientRepository;
import cat.copernic.easytraza.repository.ProducteFinalRepository;
import cat.copernic.easytraza.service.ProducteFinalService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 *
 * @author orjon
 */
@Service
public class ProducteFinalServiceImpl implements ProducteFinalService {

    private final ProducteFinalRepository producteFinalRepository;
    private final LiniaAlbaraClientRepository liniaRepo;
    /**
     * Executa l'operació ProducteFinalServiceImpl.
     * @param producteFinalRepository
     * @param liniaRepo
     */

    public ProducteFinalServiceImpl(ProducteFinalRepository producteFinalRepository,
            LiniaAlbaraClientRepository liniaRepo) {
        this.producteFinalRepository = producteFinalRepository;
        this.liniaRepo = liniaRepo;
    }
    /**
     * Consulta dades i retorna la informació necessària per a la vista o l'API.
     * @return 
     */

    @Override
    public List<ProducteFinal> findAll() {
        return producteFinalRepository.findAll();
    }
    /**
     * Valida i desa la informació rebuda.
     * @param producteFinal
     * @return 
     */

    @Override
    public ProducteFinal save(ProducteFinal producteFinal) {
        if (producteFinalRepository.existsByNom(producteFinal.getNom())) {
            throw new RuntimeException("Ja existeix un producte final amb aquest nom");
        }

        return producteFinalRepository.save(producteFinal);
    }
    /**
     * Actualitza una entitat existent amb les dades indicades.
     * @param id
     * @param producteFinal
     * @return 
     */

    @Override
    public ProducteFinal update(Long id, ProducteFinal producteFinal) {
        Optional<ProducteFinal> existent = producteFinalRepository.findById(id);

        if (existent.isEmpty()) {
            throw new RuntimeException("El producte final no existeix");
        }

        if (producteFinalRepository.existsByNomAndIdNot(producteFinal.getNom(), id)) {
            throw new RuntimeException("Ja existeix un producte final amb aquest nom");
        }

        ProducteFinal actual = existent.get();
        actual.setNom(producteFinal.getNom());
        actual.setDescripcio(producteFinal.getDescripcio());

        return producteFinalRepository.save(actual);
    }
    /**
     * Consulta dades i retorna la informació necessària per a la vista o l'API.
     * @param id
     * @return 
     */

    @Override
    public Optional<ProducteFinal> findById(Long id) {
        return producteFinalRepository.findById(id);
    }
    /**
     * Elimina o desactiva el registre indicat segons les regles de negoci.
     * @param id
     * @return 
     */

    @Override
    public String deleteById(Long id) {
        ProducteFinal producte = producteFinalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producte final no trobat"));

        boolean teLinies = liniaRepo.existsByProducteId(id);

        if (teLinies) {
            producte.setActiu(false);
            producteFinalRepository.save(producte);
            return "Aquest producte final té dades associades i s'ha desactivat.";
        }

        producteFinalRepository.deleteById(id);
        return "Producte final eliminat correctament.";
    }
    /**
     * Activa el registre indicat.
     * @param id
     */

    @Override
    public void activar(Long id) {
        ProducteFinal producte = producteFinalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producte final no trobat"));

        producte.setActiu(true);
        producteFinalRepository.save(producte);
    }
}
