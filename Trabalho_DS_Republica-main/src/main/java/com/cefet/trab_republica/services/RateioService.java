package com.cefet.trab_republica.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cefet.trab_republica.entities.Rateio;
import com.cefet.trab_republica.repositories.RateioRepository;
import org.springframework.transaction.annotation.Transactional; // Importar

@Service
public class RateioService {

    @Autowired
    private RateioRepository rateioRepository;

    @Autowired // <--- NOVO: Injetar ContaService
    private ContaService contaService;

    public List<Rateio> listarRateios() {
        return rateioRepository.findAll();
    }

    public Rateio buscarRateio(Long id) {
        return rateioRepository.findById(id).orElse(null);
    }

    @Transactional // <--- Adicionado para garantir que a operação seja atômica
    public Rateio criarRateio(Rateio rateio) {
        Rateio criado = rateioRepository.save(rateio);
        // Não é necessário verificar aqui, pois ao criar, o status é PENDENTE/EM_ABERTO.
        return criado;
    }

    @Transactional // <--- Adicionado para garantir que a operação seja atômica
    public Rateio atualizarRateio(Long id, Rateio dadosAtualizados) {
        Rateio existente = rateioRepository.findById(id).orElse(null);
        if (existente == null) {
            return null;
        }
        existente.setValor(dadosAtualizados.getValor());
        existente.setStatus(dadosAtualizados.getStatus()); // Atualiza o status do rateio

        Rateio atualizado = rateioRepository.save(existente); // Salva o rateio atualizado

        // <--- NOVO: Chamar o serviço de Conta para verificar se todos os rateios estão PAGO
        // Isso deve ser feito APÓS o rateio ser salvo.
        if (atualizado.getConta() != null) {
            contaService.verificarQuitacaoConta(atualizado.getConta().getId());
        }

        return atualizado;
    }

    @Transactional
    public void deletarRateio(Long id) {
        Rateio existente = rateioRepository.findById(id).orElse(null);
        if (existente != null) {
            rateioRepository.deleteById(id);
            // Opcional: Re-verificar a conta principal se um rateio for deletado
            if (existente.getConta() != null) {
                contaService.verificarQuitacaoConta(existente.getConta().getId());
            }
        }
    }
}