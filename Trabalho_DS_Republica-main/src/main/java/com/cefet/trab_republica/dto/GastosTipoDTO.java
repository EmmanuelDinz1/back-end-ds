package com.cefet.trab_republica.dto;

public class GastosTipoDTO {
    private final String tipoDescricao;
    private final Double total;

    public GastosTipoDTO(String tipoDescricao, Double total) {
        this.tipoDescricao = tipoDescricao;
        this.total         = total;
    }

    public String getTipoDescricao() { return tipoDescricao; }
    public Double getTotal() { return total; }
}
