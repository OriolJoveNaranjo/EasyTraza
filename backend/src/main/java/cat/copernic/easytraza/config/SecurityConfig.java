package cat.copernic.easytraza.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author orjon
 */
@Configuration
public class SecurityConfig {

    /**
     * Configura la seguretat web de l'aplicació.
     * <p>
     * Manté CSRF actiu per a les pantalles web i l'ignora només per als
     * endpoints REST sota {@code /api/**}, necessaris per a l'aplicació mòbil.
     *
     * @param http configuració de seguretat HTTP de Spring Security
     * @return cadena de filtres de seguretat
     * @throws Exception si no es pot construir la configuració de seguretat
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**")
                )
                .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/api/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/**").permitAll()
                .requestMatchers(
                        "/login",
                        "/restablir-contrasenya/**",
                        "/recuperar-contrasenya/**",
                        "/css/**",
                        "/images/**",
                        "/js/**",
                        "/uploads/**",
                        "/error",
                        "/error/**"
                ).permitAll()
                .requestMatchers(
                        "/cataleg/**",
                        "/usuaris/**",
                        "/proveidors/**",
                        "/materies-primeres/**",
                        "/clients/**"
                ).hasRole("ADMIN")
                .requestMatchers(
                        "/perfil/**",
                        "/controls/**",
                        "/albarans-proveidor/**",
                        "/lots-proveidor/**",
                        "/albarans-client/**",
                        "/informes/**",
                        "/tracabilitat/**",
                        "/grafic-productes/**",
                        "/panell/**"
                ).hasAnyRole("ADMIN", "OPERARI")
                .anyRequest().authenticated()
                )
                .formLogin(login -> login
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/panell", true)
                .permitAll()
                )
                .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
                );

        return http.build();
    }

    /**
     * Proporciona el codificador de contrasenyes utilitzat per desar i validar usuaris.
     *
     * @return codificador BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
