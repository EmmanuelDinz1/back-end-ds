package com.cefet.trab_republica.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.cefet.trab_republica.config.SecurityFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity // Habilita a segurança web do Spring
@EnableMethodSecurity // Habilita segurança em nível de método (ex: @PreAuthorize)
public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter;

    // Bean para o PasswordEncoder (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean para o AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desabilita CSRF para APIs REST
                .cors(Customizer.withDefaults()) // Habilita CORS (configurado no bean abaixo)
                .headers(headers -> headers.frameOptions(frame -> frame.disable())) // Necessário para H2 Console
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Garante que não há sessão de estado (para JWT)

        // Adiciona o SecurityFilter customizado ANTES do filtro de autenticação padrão do Spring.
        // Ele é o responsável por autenticar requisições com o token JWT.
        http.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        // Configuração de autorização de requisições
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Permite requisições OPTIONS (para CORS preflight)
                .requestMatchers("/h2-console/**").permitAll() // Permite acesso ao H2 Console
                // <--- ESSENCIAL: Permite acesso a todos os endpoints de /api/moradores (cadastro e login)
                .requestMatchers("/api/moradores/**").permitAll()
                // Todas as outras requisições (que NÃO foram permitAll acima) exigem autenticação
                .anyRequest().authenticated()
        );

        return http.build();
    }

    // Configuração de CORS
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:5173", "http://localhost:3000", "https://trabalho-ds-republica.onrender.com"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}