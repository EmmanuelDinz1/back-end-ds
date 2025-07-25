package com.cefet.trab_republica.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cefet.trab_republica.entities.TipoConta;
import com.cefet.trab_republica.repositories.TipoContaRepository;

@Service
public class TipoContaService {

    @Autowired
    private TipoContaRepository tipoContaRepository;

    public TipoConta criarTipoConta(TipoConta tipoConta) {
        return tipoContaRepository.save(tipoConta);
    }

    public List<TipoConta> listarTiposConta() {
        return tipoContaRepository.findAll();
    }

    public TipoConta buscarTipoConta(Long id) {
        return tipoContaRepository.findById(id).orElse(null);
    }

    public TipoConta atualizarTipoConta(Long id, TipoConta dadosAtualizados) {
        TipoConta existente = tipoContaRepository.findById(id).orElse(null);
        if (existente == null) {
            return null;
        }
        existente.setDescricao(dadosAtualizados.getDescricao());
        existente.setObservacao(dadosAtualizados.getObservacao());
        return tipoContaRepository.save(existente);
    }

    public void deletarTipoConta(Long id) {
        tipoContaRepository.deleteById(id);
    }
}