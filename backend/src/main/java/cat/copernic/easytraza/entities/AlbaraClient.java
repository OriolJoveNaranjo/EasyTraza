package cat.copernic.easytraza.entities;

import cat.copernic.easytraza.enums.EstatAlbaraClient;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author orjon
 */
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = {"client_id", "data"})
)
/**
 * Entitat JPA que representa AlbaraClient dins del domini d'EasyTraza.
 */
@Entity
public class AlbaraClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime data;

    @ManyToOne(optional = false)
    private Client client;

    @Enumerated(EnumType.STRING)
    private EstatAlbaraClient estat;

    @OneToMany(mappedBy = "albaraClient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LiniaAlbaraClient> linies = new ArrayList<>();
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public List<LiniaAlbaraClient> getLinies() {
        return linies;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param linies
     */

    public void setLinies(List<LiniaAlbaraClient> linies) {
        this.linies = linies;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public Long getId() {
        return id;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param id
     */

    public void setId(Long id) {
        this.id = id;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public LocalDateTime getData() {
        return data;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param data
     */

    public void setData(LocalDateTime data) {
        this.data = data;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public Client getClient() {
        return client;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param client
     */

    public void setClient(Client client) {
        this.client = client;
    }
    /**
     * Retorna el valor de la propietat indicada.
     * @return 
     */

    public EstatAlbaraClient getEstat() {
        return estat;
    }
    /**
     * Actualitza el valor de la propietat indicada.
     * @param estat
     */

    public void setEstat(EstatAlbaraClient estat) {
        this.estat = estat;
    }

}
