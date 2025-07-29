package com.cefet.trab_republica.services;

import com.cefet.trab_republica.entities.Morador;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(Morador morador) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("TrabalhoPW") // Emissor do token

                    .withSubject(morador.getEmail()) // Assunto do token (email do morador)
                    .withExpiresAt(gerarDataExpiracao()) // Data de expiração
                    .sign(algorithm); // Assina o token com o algoritmo e a chave secreta
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("TrabalhoPW") // Verifica o emissor
                    .build()
                    .verify(token) // Verifica a assinatura e validade do token
                    .getSubject(); // Retorna o assunto (email do morador)
        } catch (JWTVerificationException exception) {
            return null; // Token inválido ou expirado
        }
    }

    private Instant gerarDataExpiracao() {
        return LocalDateTime.now()
                .plusHours(2) // Token válido por 2 horas
                .toInstant(ZoneOffset.of("-03:00")); // Offset de fuso horário
    }
}