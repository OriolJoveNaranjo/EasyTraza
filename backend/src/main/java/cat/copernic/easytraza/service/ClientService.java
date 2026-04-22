/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.service;

import cat.copernic.easytraza.entities.Client;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author orjon
 */
public interface ClientService {

    List<Client> findAll();

    Optional<Client> findById(Long id);

    Client save(Client client);

    Client update(Long id, Client client);

    void deleteById(Long id);
}
