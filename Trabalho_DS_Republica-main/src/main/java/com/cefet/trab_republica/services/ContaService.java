package com.cefet.trab_republica.services;

import com.cefet.trab_republica.dto.ContaDTO;
import com.cefet.trab_republica.dto.GastosTipoDTO;
import com.cefet.trab_republica.entities.Conta;
import com.cefet.trab_republica.entities.HistoricoConta;
import com.cefet.trab_republica.entities.Rateio;
import com.cefet.trab_republica.entities.SituacaoConta;
import com.cefet.trab_republica.entities.StatusRateio;
import com.cefet.trab_republica.repositories.ContaRepository;
import com.cefet.trab_republica.repositories.HistoricoContaRepository;
import com.cefet.trab_republica.repositories.RateioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional; // Importar Optional

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private RateioRepository rateioRepository;

    @Autowired
    private HistoricoContaRepository historicoRepository;

    /**
     * Cria uma nova conta, configura situação, persiste sem rateios,
     * depois salva cada Rateio manualmente e registra histórico.
     */
    @Transactional
    public Conta criarConta(Conta conta) {
        // define situação inicial
        conta.setSituacao(SituacaoConta.PENDENTE);

        // extrai e zera os rateios temporariamente
        List<Rateio> novosRateios = conta.getRateios();
        conta.setRateios(Collections.emptyList());

        // salva a conta sem rateios
        Conta salva = contaRepository.save(conta);

        // agora persiste cada rateio, vinculando à conta salva
        if (novosRateios != null) {
            for (Rateio r : novosRateios) {
                r.setConta(salva);
                if (r.getStatus() == null) {
                    r.setStatus(StatusRateio.EM_ABERTO);
                }
                rateioRepository.save(r);
            }
        }

        // cria e salva histórico de criação
        HistoricoConta hist = new HistoricoConta();
        hist.setConta(salva);
        hist.setMorador(salva.getResponsavel());
        hist.setAcao(SituacaoConta.PENDENTE);
        hist.setTimestamp(Instant.now());
        historicoRepository.save(hist);

        // Verifica a quitação da conta após criação (se já veio com rateios pagos)
        verificarQuitacaoConta(salva.getId());

        return salva;
    }

    @Transactional(readOnly = true)
    public List<ContaDTO> listarContasDTO() {
        return contaRepository.findAll()
                .stream()
                .map(ContaDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public ContaDTO buscarContaDTO(Long id) {
        return contaRepository.findById(id)
                .map(ContaDTO::new)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<ContaDTO> extratoDTO(LocalDate inicio, LocalDate fim) {
        return contaRepository.findByDataVencimentoBetween(inicio, fim)
                .stream()
                .map(ContaDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ContaDTO> listarContasEmAbertoDTO() {
        return contaRepository.findContasEmAberto()
                .stream()
                .map(ContaDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GastosTipoDTO> gastosPorTipo() {
        return contaRepository.totalGastosPorTipo();
    }

    @Transactional
    public Conta atualizarConta(Long id, Conta dadosAtualizados) {
        Conta existente = contaRepository.findById(id).orElse(null);
        if (existente == null) {
            return null;
        }
        if (existente.getSituacao() == SituacaoConta.QUITADA ||
                existente.getSituacao() == SituacaoConta.CANCELADA) {
            throw new RuntimeException("Não é possível editar conta quitada ou cancelada.");
        }

        // atualiza campos básicos
        existente.setObservacao(dadosAtualizados.getObservacao());
        existente.setValor(dadosAtualizados.getValor());
        existente.setDataVencimento(dadosAtualizados.getDataVencimento());
        existente.setTipoConta(dadosAtualizados.getTipoConta());
        existente.setResponsavel(dadosAtualizados.getResponsavel());

        // remove antigos rateios
        rateioRepository.deleteAll(existente.getRateios());
        existente.getRateios().clear();

        // persiste novos rateios
        if (dadosAtualizados.getRateios() != null) {
            for (Rateio r : dadosAtualizados.getRateios()) {
                r.setConta(existente);
                if (r.getStatus() == null) {
                    r.setStatus(StatusRateio.EM_ABERTO); // Ou StatusRateio.PENDENTE
                }
                rateioRepository.save(r);
            }
        }

        // salva a conta atualizada
        Conta atualizada = contaRepository.save(existente);

        // registra histórico de atualização
        HistoricoConta hist = new HistoricoConta();
        hist.setConta(atualizada);
        hist.setMorador(atualizada.getResponsavel());
        hist.setAcao(atualizada.getSituacao()); // Ação é a situação atual da conta
        hist.setTimestamp(Instant.now());
        historicoRepository.save(hist);

        // <--- NOVO: Chamar o método para verificar a quitação da conta principal
        verificarQuitacaoConta(atualizada.getId());

        return atualizada;
    }

    @Transactional
    public void deletarConta(Long id) {
        Conta existente = contaRepository.findById(id).orElse(null);
        if (existente != null) {
            // Remove a conta
            contaRepository.deleteById(id);

            // Registra histórico de cancelamento
            HistoricoConta hist = new HistoricoConta();
            hist.setConta(existente);
            hist.setMorador(existente.getResponsavel());
            hist.setAcao(SituacaoConta.CANCELADA);
            hist.setTimestamp(Instant.now());
            historicoRepository.save(hist);
        }
    }

    @Transactional
    public Conta replicarConta(Long id, Conta dadosAlterados) {
        Conta original = contaRepository.findById(id).orElse(null);
        if (original == null) {
            return null;
        }

        // monta cópia
        Conta copia = new Conta();
        copia.setObservacao(original.getObservacao());
        copia.setValor(original.getValor());
        copia.setDataVencimento(dadosAlterados.getDataVencimento());
        copia.setTipoConta(original.getTipoConta());
        copia.setResponsavel(dadosAlterados.getResponsavel());
        copia.setSituacao(SituacaoConta.PENDENTE);

        // salva cópia sem rateios
        Conta salva = contaRepository.save(copia);

        // replica rateios
        for (Rateio rOld : original.getRateios()) {
            Rateio novo = new Rateio();
            novo.setConta(salva);
            novo.setMorador(rOld.getMorador());
            novo.setValor(rOld.getValor());
            novo.setStatus(StatusRateio.PENDENTE);
            rateioRepository.save(novo);
        }

        // registra histórico de replicação
        HistoricoConta hist = new HistoricoConta();
        hist.setConta(salva);
        hist.setMorador(salva.getResponsavel());
        hist.setAcao(SituacaoConta.PENDENTE);
        hist.setTimestamp(Instant.now());
        historicoRepository.save(hist);

        return salva;
    }

    // <--- NOVO MÉTODO: Verifica se todos os rateios de uma conta foram pagos
    @Transactional
    public void verificarQuitacaoConta(Long contaId) {
        Optional<Conta> contaOptional = contaRepository.findById(contaId);
        if (contaOptional.isEmpty()) {
            return;
        }
        Conta conta = contaOptional.get();

        // Se a conta já está quitada ou cancelada, não faz nada
        if (conta.getSituacao() == SituacaoConta.QUITADA || conta.getSituacao() == SituacaoConta.CANCELADA) {
            return;
        }

        // Busca todos os rateios da conta
        List<Rateio> rateiosDaConta = rateioRepository.findByContaId(contaId);

        // Verifica se TODOS os rateios estão PAGO
        boolean todosPagos = rateiosDaConta.stream()
                .allMatch(rateio -> rateio.getStatus() == StatusRateio.PAGO);

        if (todosPagos && !rateiosDaConta.isEmpty()) { // Garante que há rateios e todos foram pagos
            conta.setSituacao(SituacaoConta.QUITADA);
            contaRepository.save(conta);

            // Registra histórico de quitação
            HistoricoConta hist = new HistoricoConta();
            hist.setConta(conta);
            hist.setMorador(conta.getResponsavel()); // O responsável pela conta registrou a quitação
            hist.setAcao(SituacaoConta.QUITADA);
            hist.setTimestamp(Instant.now());
            historicoRepository.save(hist);
            System.out.println(">>> ContaService: Conta " + conta.getId() + " automaticamente quitada.");

        } else if (conta.getSituacao() == SituacaoConta.QUITADA && !todosPagos) {
            // Se a conta estava quitada, mas um rateio foi reaberto, volta para PENDENTE
            conta.setSituacao(SituacaoConta.PENDENTE);
            contaRepository.save(conta);

            HistoricoConta hist = new HistoricoConta();
            hist.setConta(conta);
            hist.setMorador(conta.getResponsavel());
            hist.setAcao(SituacaoConta.PENDENTE);
            hist.setTimestamp(Instant.now());
            historicoRepository.save(hist);
            System.out.println(">>> ContaService: Conta " + conta.getId() + " automaticamente reaberta para PENDENTE.");
        }
    }
}