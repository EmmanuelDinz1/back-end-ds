package com.cefet.trab_republica.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tb_conta")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String observacao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_conta_id")
    private TipoConta tipoConta;

    @Column(nullable = false)
    private double valor;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @ManyToOne(optional = false)
    @JoinColumn(name = "responsavel_id")
    private Morador responsavel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SituacaoConta situacao;

    /**
     * Rateios NÃO em cascade: serão persistidos manualmente no service,
     * para garantir que 'conta' já exista.
     */
    @OneToMany(mappedBy = "conta", orphanRemoval = true)
    private List<Rateio> rateios;

    /**
     * Histórico pode ser em cascade, pois só se cria após Conta persistida.
     */
    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoricoConta> historico;

    // ————————————————
    // Getters & Setters
    // ————————————————

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @Transient
    public String getDescricao() {
        return observacao;
    }

    public TipoConta getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(TipoConta tipoConta) {
        this.tipoConta = tipoConta;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Morador getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Morador responsavel) {
        this.responsavel = responsavel;
    }

    public SituacaoConta getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoConta situacao) {
        this.situacao = situacao;
    }

    public List<Rateio> getRateios() {
        return rateios;
    }

    public void setRateios(List<Rateio> rateios) {
        this.rateios = rateios;
    }

    public List<HistoricoConta> getHistorico() {
        return historico;
    }

    public void setHistorico(List<HistoricoConta> historico) {
        this.historico = historico;
    }
}
