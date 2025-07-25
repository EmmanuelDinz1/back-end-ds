package com.cefet.trab_republica.dto;

import java.time.LocalDate;
import com.cefet.trab_republica.entities.Conta;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ContaDTO {
    private final Long id;
    private final String observacao;
    private final double valor;
    private final LocalDate dataVencimento;
    private final Long responsavelId;
    private final String responsavelNome;
    private final Long tipoContaId;
    private final String tipoContaDescricao;
    private final String situacao;
    private final List<RateioDTO> rateios;

    public ContaDTO(Conta c) {
        this.id                   = c.getId();
        this.observacao           = c.getObservacao();
        this.valor                = c.getValor();
        this.dataVencimento       = c.getDataVencimento();
        this.responsavelId        = c.getResponsavel().getId();
        this.responsavelNome      = c.getResponsavel().getNome();
        this.tipoContaId          = c.getTipoConta().getId();
        this.tipoContaDescricao   = c.getTipoConta().getDescricao();
        this.situacao             = c.getSituacao().name();
        this.rateios              = c.getRateios()
                                       .stream()
                                       .map(RateioDTO::new)
                                       .collect(Collectors.toList());
    }

    public Long getId() { return id; }
    public String getObservacao() { return observacao; }
    public double getValor() { return valor; }
    public LocalDate getDataVencimento() { return dataVencimento; }
    public Long getResponsavelId() { return responsavelId; }
    public String getResponsavelNome() { return responsavelNome; }
    public Long getTipoContaId() { return tipoContaId; }
    public String getTipoContaDescricao() { return tipoContaDescricao; }
    public String getSituacao() { return situacao; }
    public List<RateioDTO> getRateios() { return rateios; }
}

