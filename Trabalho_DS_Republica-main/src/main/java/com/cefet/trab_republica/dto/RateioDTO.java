package com.cefet.trab_republica.dto;
import com.cefet.trab_republica.entities.Rateio;

public class RateioDTO {
    private final Long id;
    private final Long moradorId;
    private final String moradorNome;
    private final double valor;
    private final String status;

    public RateioDTO(Rateio r) {
        this.id          = r.getId();
        this.moradorId   = r.getMorador().getId();
        this.moradorNome = r.getMorador().getNome();
        this.valor       = r.getValor();
        this.status      = r.getStatus().name();
    }

    public Long getId() { return id; }
    public Long getMoradorId() { return moradorId; }
    public String getMoradorNome() { return moradorNome; }
    public double getValor() { return valor; }
    public String getStatus() { return status; }
}


