package cat.copernic.easytraza.config;

import cat.copernic.easytraza.entities.Usuari;
import cat.copernic.easytraza.repository.UsuariRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author orjo Servicio que permite a Spring Security buscar usuarios por
 * email.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuariRepository usuariRepository;
    /**
     * Executa l'operació CustomUserDetailsService.
     * @param usuariRepository
     */

    public CustomUserDetailsService(UsuariRepository usuariRepository) {
        this.usuariRepository = usuariRepository;
    }
    /**
     * Executa l'operació loadUserByUsername.
     * @param email
     * @return 
     */

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuari usuari = usuariRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuari no trobat"));

        return User.builder()
                .username(usuari.getEmail())
                .password(usuari.getPassword())
                .disabled(!usuari.isActiu())
                .roles(usuari.getRol().name())
                .build();
    }
}
