package com.cefet.trab_republica.dto;

public class LoginResponseDTO {
    private String status; // Ex: "Autenticado com sucesso", "Credenciais inválidas"
    private Long moradorId;
    private String moradorNome;
    private String token; // Novo campo para o token JWT

    public LoginResponseDTO(String status, Long moradorId, String moradorNome, String token) {
        this.status = status;
        this.moradorId = moradorId;
        this.moradorNome = moradorNome;
        this.token = token;
    }

    // Getters e Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getMoradorId() {
        return moradorId;
    }

    public void setMoradorId(Long moradorId) {
        this.moradorId = moradorId;
    }

    public String getMoradorNome() {
        return moradorNome;
    }

    public void setMoradorNome(String moradorNome) {
        this.moradorNome = moradorNome;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}