/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.service.impl;

import cat.copernic.easytraza.entities.Usuari;
import cat.copernic.easytraza.repository.UsuariRepository;
import cat.copernic.easytraza.service.UsuariService;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author orjon Implementació del servei d'usuaris.
 */
@Service
public class UsuariServiceImpl implements UsuariService {

    private final UsuariRepository usuariRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuariServiceImpl(UsuariRepository usuariRepository, PasswordEncoder passwordEncoder) {
        this.usuariRepository = usuariRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Usuari> findAll() {
        return usuariRepository.findAll();
    }

    @Override
    public Optional<Usuari> findById(Long id) {
        return usuariRepository.findById(id);
    }

    @Override
    public Usuari save(Usuari usuari) {
        if (usuariRepository.existsByEmail(usuari.getEmail())) {
            throw new RuntimeException("Ja existeix un usuari amb aquest email");
        }
        usuari.setPassword(passwordEncoder.encode(usuari.getPassword()));

        return usuariRepository.save(usuari);
    }

    @Override
    public Usuari update(Long id, Usuari usuari) {
        Optional<Usuari> existent = usuariRepository.findById(id);

        if (existent.isEmpty()) {
            throw new RuntimeException("L'usuari no existeix");
        }

        if (usuariRepository.existsByEmailAndIdNot(usuari.getEmail(), id)) {
            throw new RuntimeException("Ja existeix un usuari amb aquest email");
        }

        Usuari actual = existent.get();
        actual.setNom(usuari.getNom());
        actual.setRol(usuari.getRol());
        actual.setActiu(usuari.isActiu());
        if (usuari.getPassword() != null && !usuari.getPassword().trim().isEmpty()) {
            actual.setPassword(passwordEncoder.encode(usuari.getPassword()));
        }

        return usuariRepository.save(actual);
    }

    @Override
    public void deleteById(Long id) {
        usuariRepository.deleteById(id);
    }
}
