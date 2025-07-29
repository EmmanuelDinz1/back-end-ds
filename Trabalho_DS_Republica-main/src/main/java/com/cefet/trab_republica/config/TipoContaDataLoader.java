package com.cefet.trab_republica.config;

import com.cefet.trab_republica.entities.TipoConta;
import com.cefet.trab_republica.repositories.TipoContaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class TipoContaDataLoader {

    @Bean
    public CommandLineRunner initTipoContaData(TipoContaRepository tipoContaRepository) {
        return args -> {
            List<String> tiposParaAdicionar = Arrays.asList(
                    "ALUGUEL",
                    "AGUA",
                    "LUZ",
                    "INTERNET",
                    "ALIMENTACAO",
                    "SERVICOS_STREAMING",
                    "OUTROS"
            );

            for (String tipoDescricao : tiposParaAdicionar) {
                // Verifica se o tipo de conta já existe antes de adicionar
                if (tipoContaRepository.findByDescricao(tipoDescricao).isEmpty()) {
                    TipoConta novoTipo = new TipoConta();
                    novoTipo.setDescricao(tipoDescricao);
                    // Opcional: Adicione uma observação se desejar
                    // novoTipo.setObservacao("Descrição padrão para " + tipoDescricao);
                    tipoContaRepository.save(novoTipo);
                    System.out.println("Tipo de Conta adicionado: " + tipoDescricao);
                }
            }
        };
    }
}