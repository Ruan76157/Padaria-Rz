package com.javaRz.padaria.infra;

import com.javaRz.padaria.infrastructure.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    private final UserRepository repository;

    public SecurityConfigurations(UserRepository repository) {
        this.repository = repository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // H2 Console
                        .requestMatchers("/h2-console/**").permitAll()

                        // Rotas antigas (sem /api)
                        .requestMatchers("/usuario/**").permitAll()
                        .requestMatchers("/cadastro/**").permitAll()
                        .requestMatchers("/padaria/**").permitAll()
                        .requestMatchers("/compras/**").permitAll()

                        // Rotas da API (com /api)
                        .requestMatchers("/api/usuarios/**").permitAll()
                        .requestMatchers("/api/cadastros/**").permitAll()
                        .requestMatchers("/api/padarias/**").permitAll()
                        .requestMatchers("/api/compras/**").permitAll()
                        .requestMatchers("/api/produtos/**").permitAll() // ADICIONEI ESTA LINHA

                        // Uploads e arquivos estáticos
                        .requestMatchers("/uploads/**").permitAll()

                        .anyRequest().authenticated()
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )
                .build();
    }
}