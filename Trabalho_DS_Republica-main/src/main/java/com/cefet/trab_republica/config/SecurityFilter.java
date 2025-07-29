package com.cefet.trab_republica.config;

import com.cefet.trab_republica.entities.Morador;
import com.cefet.trab_republica.repositories.MoradorRepository;
import com.cefet.trab_republica.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.http.HttpMethod;

// ... (imports) ...
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.http.HttpMethod; // Para HttpMethod

import java.io.IOException;
import java.util.Collections;
import java.util.Optional; // Já deve estar lá

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final MoradorRepository moradorRepository;

    // Define os RequestMatchers para os caminhos que devem ser PUBLICOS
    // ESTES DEVEM SER OS MESMOS QUE ESTÃO COM .permitAll() NO SecurityConfig
    private final RequestMatcher publicPaths = new OrRequestMatcher(
            // Permite POST para /api/moradores (cadastro) e /api/moradores/auth (login)
            new AntPathRequestMatcher("/api/moradores", HttpMethod.POST.name()),
            new AntPathRequestMatcher("/api/moradores/auth", HttpMethod.POST.name()),

            new AntPathRequestMatcher("/h2-console/**"),     // H2 Console
            new AntPathRequestMatcher("/**", HttpMethod.OPTIONS.name()) // CORS pre-flight OPTIONS requests
    );

    @Autowired
    public SecurityFilter(TokenService tokenService, MoradorRepository moradorRepository) {
        this.tokenService = tokenService;
        this.moradorRepository = moradorRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Se a requisição for para um caminho público, o filtro PULA a autenticação
        if (publicPaths.matches(request)) {
            System.out.println(">>> SecurityFilter - Requisição para caminho público, pulando autenticação: " + request.getRequestURI() + " " + request.getMethod());
            filterChain.doFilter(request, response);
            return; // Sai do método do filtro para esta requisição
        }

        // Tenta autenticar requisições para caminhos PROTEGIDOS (os que não foram permitAll)
        String authHeader = request.getHeader("Authorization");
        System.out.println(">>> SecurityFilter - Header Authorization para rota protegida: " + (authHeader != null ? authHeader.substring(0, Math.min(authHeader.length(), 50)) : "NULO"));

        String token = recoverToken(request); // Recupera o token JWT do header "Bearer "
        System.out.println(">>> SecurityFilter - Token extraído para rota protegida: " + token);

        if (token != null) {
            try {
                String email = tokenService.validateToken(token); // Valida o token JWT e extrai o email
                System.out.println(">>> SecurityFilter - Email extraído do token para rota protegida: " + email);

                if (email != null) {
                    Optional<Morador> moradorOptional = moradorRepository.findByEmail(email); // Busca o morador pelo email do token

                    if (moradorOptional.isPresent()) {
                        Morador morador = moradorOptional.get();
                        // Cria o objeto de autenticação com o email (principal) e authorities (ROLES)
                        var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
                        var authentication = new UsernamePasswordAuthenticationToken(morador.getEmail(), null, authorities);

                        // Define o usuário no contexto de segurança do Spring
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        System.out.println(">>> SecurityFilter - Usuário autenticado com sucesso no contexto para rota protegida: " + morador.getEmail());
                    } else {
                        System.out.println(">>> SecurityFilter - Usuário do token não encontrado no banco de dados para rota protegida: " + email);
                    }
                } else {
                    System.out.println(">>> SecurityFilter - Token inválido ou expirado. Email nulo.");
                }

            } catch (Exception e) { // Captura exceções da validação do JWT
                System.err.println(">>> SecurityFilter - Erro inesperado ao autenticar token para rota protegida: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
                return;
            }
        } else {
            System.out.println(">>> SecurityFilter - Nenhum token JWT fornecido no Authorization header para rota protegida.");
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7).trim();
        }
        return null;
    }
}