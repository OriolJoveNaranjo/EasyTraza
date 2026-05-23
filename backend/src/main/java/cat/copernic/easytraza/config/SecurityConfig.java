/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.easytraza.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
     * Executa l'operació securityFilterChain.
     */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**")
                )
                .authorizeHttpRequests(auth -> auth
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/**").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/**").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/**").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/**").permitAll()
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
     * Executa l'operació passwordEncoder.
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
