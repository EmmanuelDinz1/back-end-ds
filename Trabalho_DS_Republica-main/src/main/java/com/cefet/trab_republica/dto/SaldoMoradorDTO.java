package com.cefet.trab_republica.dto;

public class SaldoMoradorDTO {
    private final Long moradorId;
    private final String nome;
    private final Double saldoDevedor;

    public SaldoMoradorDTO(Long moradorId, String nome, Double saldoDevedor) {
        this.moradorId   = moradorId;
        this.nome        = nome;
        this.saldoDevedor = saldoDevedor;
    }

    public Long getMoradorId() { return moradorId; }
    public String getNome() { return nome; }
    public Double getSaldoDevedor() { return saldoDevedor; }
}
