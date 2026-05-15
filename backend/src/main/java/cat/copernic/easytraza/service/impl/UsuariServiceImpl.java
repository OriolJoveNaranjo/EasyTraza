/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.service.impl;

import cat.copernic.easytraza.entities.Usuari;
import cat.copernic.easytraza.repository.AlbaraProveidorRepository;
import cat.copernic.easytraza.repository.ControlPhRepository;
import cat.copernic.easytraza.repository.LotProveidorRepository;
import cat.copernic.easytraza.repository.PasswordResetTokenRepository;
import cat.copernic.easytraza.repository.UsuariRepository;
import cat.copernic.easytraza.service.UsuariService;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author orjon Implementació del servei d'usuaris.
 */
@Service
public class UsuariServiceImpl implements UsuariService {

    private final UsuariRepository usuariRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final LotProveidorRepository lotProveidorRepo;
    private final AlbaraProveidorRepository albaraProveidorRepo;
    private final ControlPhRepository controlPhRepo;

    public UsuariServiceImpl(UsuariRepository usuariRepository, PasswordEncoder passwordEncoder,
            LotProveidorRepository lotProveidorRepo, AlbaraProveidorRepository albaraProveidorRepo,
            ControlPhRepository controlPhRepo, PasswordResetTokenRepository passwordResetTokenRepository) {
        this.usuariRepository = usuariRepository;
        this.passwordEncoder = passwordEncoder;
        this.lotProveidorRepo = lotProveidorRepo;
        this.albaraProveidorRepo = albaraProveidorRepo;
        this.controlPhRepo = controlPhRepo;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
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
    @Transactional
    public String deleteById(Long id) {
        Usuari usuari = usuariRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuari no trobat"));

        boolean teLots = lotProveidorRepo.existsByUsuariOberturaId(id);
        boolean teAlbaransProveidor = albaraProveidorRepo.existsByUsuariAltaId(id);
        boolean teControls = controlPhRepo.existsByUsuariId(id);

        passwordResetTokenRepository.deleteByUsuari(usuari);

        if (teLots || teAlbaransProveidor || teControls) {
            usuari.setActiu(false);
            usuariRepository.save(usuari);
            return "Aquest usuari no es pot eliminar perquè té dades associades, però s'ha desactivat.";
        }

        usuariRepository.deleteById(id);
        return "Usuari eliminat correctament.";
    }

    @Override
    public void activar(Long id) {
        Usuari usuari = usuariRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuari no trobat"));

        usuari.setActiu(true);
        usuariRepository.save(usuari);
    }
}
