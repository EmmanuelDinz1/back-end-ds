package com.cefet.trab_republica.controllers;

import com.cefet.trab_republica.dto.HistoricoContaDTO;
import com.cefet.trab_republica.entities.HistoricoConta;
import com.cefet.trab_republica.services.HistoricoContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historicos")
public class HistoricoContaController {

    @Autowired
    private HistoricoContaService historicoService;

    /** GET /api/historicos – lista todos os históricos como DTOs */
    @GetMapping
    public ResponseEntity<List<HistoricoContaDTO>> getAllHistoricos() {
        return ResponseEntity.ok(historicoService.listarHistoricosDTO());
    }

    /** GET /api/historicos/conta/{contaId} – históricos de uma conta como DTOs */
    @GetMapping("/conta/{contaId}")
    public ResponseEntity<List<HistoricoContaDTO>> getHistoricosByConta(@PathVariable Long contaId) {
        return ResponseEntity.ok(historicoService.buscarHistoricoPorContaDTO(contaId));
    }

    /** POST /api/historicos – cria um novo histórico e retorna um DTO */
    @PostMapping
    public ResponseEntity<HistoricoContaDTO> createHistorico(@RequestBody HistoricoConta historico) {
        HistoricoContaDTO dto = historicoService.criarHistoricoDTO(historico);
        return ResponseEntity.status(201).body(dto);
    }
}
