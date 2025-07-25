package com.cefet.trab_republica.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cefet.trab_republica.entities.Rateio;
import com.cefet.trab_republica.repositories.RateioRepository;

@Service
public class RateioService {

    @Autowired
    private RateioRepository rateioRepository;

    public List<Rateio> listarRateios() {
        return rateioRepository.findAll();
    }

    public Rateio buscarRateio(Long id) {
        return rateioRepository.findById(id).orElse(null);
    }

    public Rateio criarRateio(Rateio rateio) {
        return rateioRepository.save(rateio);
    }

    public Rateio atualizarRateio(Long id, Rateio dadosAtualizados) {
        Rateio existente = rateioRepository.findById(id).orElse(null);
        if (existente == null) {
            return null;
        }
        existente.setValor(dadosAtualizados.getValor());
        existente.setStatus(dadosAtualizados.getStatus());
        return rateioRepository.save(existente);
    }

    public void deletarRateio(Long id) {
        rateioRepository.deleteById(id);
    }
}