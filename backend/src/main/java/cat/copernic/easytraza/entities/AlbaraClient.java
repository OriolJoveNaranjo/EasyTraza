/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

    public List<LiniaAlbaraClient> getLinies() {
        return linies;
    }

    public void setLinies(List<LiniaAlbaraClient> linies) {
        this.linies = linies;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public EstatAlbaraClient getEstat() {
        return estat;
    }

    public void setEstat(EstatAlbaraClient estat) {
        this.estat = estat;
    }

}
