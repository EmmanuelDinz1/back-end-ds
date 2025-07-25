package com.cefet.trab_republica.dto;
import com.cefet.trab_republica.entities.HistoricoConta;
import java.time.Instant;

public class HistoricoContaDTO {
    private final Long id;
    private final Long contaId;
    private final Long moradorId;
    private final String acao;
    private final Instant timestamp;

    public HistoricoContaDTO(HistoricoConta h) {
        this.id        = h.getId();
        this.contaId   = h.getConta().getId();
        this.moradorId = h.getMorador().getId();
        this.acao      = h.getAcao().name();
        this.timestamp = h.getTimestamp();
    }

    public Long getId() { return id; }
    public Long getContaId() { return contaId; }
    public Long getMoradorId() { return moradorId; }
    public String getAcao() { return acao; }
    public Instant getTimestamp() { return timestamp; }
}



