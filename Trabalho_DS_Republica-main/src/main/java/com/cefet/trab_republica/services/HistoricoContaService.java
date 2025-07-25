package com.cefet.trab_republica.services;

import com.cefet.trab_republica.dto.HistoricoContaDTO;
import com.cefet.trab_republica.entities.HistoricoConta;
import com.cefet.trab_republica.entities.Conta;
import com.cefet.trab_republica.entities.Morador;
import com.cefet.trab_republica.repositories.HistoricoContaRepository;
import com.cefet.trab_republica.repositories.ContaRepository;
import com.cefet.trab_republica.repositories.MoradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class HistoricoContaService {

    @Autowired
    private HistoricoContaRepository historicoRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private MoradorRepository moradorRepository;

    /**
     * Lista todos os históricos como DTOs
     */
    @Transactional(readOnly = true)
    public List<HistoricoContaDTO> listarHistoricosDTO() {
        return historicoRepository.findAll().stream()
                .map(HistoricoContaDTO::new)
                .toList();
    }

    /**
     * Busca históricos de uma conta específica como DTOs
     */
    @Transactional(readOnly = true)
    public List<HistoricoContaDTO> buscarHistoricoPorContaDTO(Long contaId) {
        return historicoRepository.findByContaId(contaId).stream()
                .map(HistoricoContaDTO::new)
                .toList();
    }

    /**
     * Cria um novo histórico e retorna como DTO.
     * Valida antes se conta e morador existem, caso contrário retorna 404.
     */
    @Transactional
    public HistoricoContaDTO criarHistoricoDTO(HistoricoConta historico) {
        Long contaId = historico.getConta().getId();
        Long moradorId = historico.getMorador().getId();

        // valida existência
        if (!contaRepository.existsById(contaId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Conta não encontrada: " + contaId);
        }
        if (!moradorRepository.existsById(moradorId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Morador não encontrado: " + moradorId);
        }

        // pega proxy sem forçar select completo
        Conta contaRef = contaRepository.getReferenceById(contaId);
        Morador moradorRef = moradorRepository.getReferenceById(moradorId);

        // monta nova instância para garantir id=null
        HistoricoConta novo = new HistoricoConta();
        novo.setConta(contaRef);
        novo.setMorador(moradorRef);
        novo.setAcao(historico.getAcao());
        novo.setTimestamp(historico.getTimestamp());

        HistoricoConta salvo = historicoRepository.save(novo);
        return new HistoricoContaDTO(salvo);
    }
}
