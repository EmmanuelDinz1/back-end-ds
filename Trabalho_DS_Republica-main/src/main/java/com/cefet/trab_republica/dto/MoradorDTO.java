package com.cefet.trab_republica.dto;

import java.time.LocalDate;
import com.cefet.trab_republica.entities.Morador;
import java.time.LocalDate;

public class MoradorDTO {
    private final Long id;
    private final String nome;
    private final String cpf;
    private final LocalDate dataNascimento;
    private final String celular;
    private final String email;
    private final String contatosFamilia;

    public MoradorDTO(Morador m) {
        this.id              = m.getId();
        this.nome            = m.getNome();
        this.cpf             = m.getCpf();
        this.dataNascimento  = m.getDataNascimento();
        this.celular         = m.getCelular();
        this.email           = m.getEmail();
        this.contatosFamilia = m.getContatosFamilia();
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public String getCelular() { return celular; }
    public String getEmail() { return email; }
    public String getContatosFamilia() { return contatosFamilia; }
}
