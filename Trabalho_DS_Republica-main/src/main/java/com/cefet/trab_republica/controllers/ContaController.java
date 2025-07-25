package com.cefet.trab_republica.controllers;

import com.cefet.trab_republica.dto.ContaDTO;
import com.cefet.trab_republica.dto.GastosTipoDTO;
import com.cefet.trab_republica.entities.Conta;
import com.cefet.trab_republica.services.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    /** GET /api/contas – lista todas as contas como DTOs */
    @GetMapping
    public ResponseEntity<List<ContaDTO>> getAllContas() {
        return ResponseEntity.ok(contaService.listarContasDTO());
    }

    /** GET /api/contas/{id} – busca uma conta por ID */
    @GetMapping("/{id}")
    public ResponseEntity<ContaDTO> getContaById(@PathVariable Long id) {
        ContaDTO dto = contaService.buscarContaDTO(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    /** GET /api/contas/extrato?dataInicio=YYYY-MM-DD&dataFim=YYYY-MM-DD – extrato por intervalo */
    @GetMapping("/extrato")
    public ResponseEntity<List<ContaDTO>> extrato(
            @RequestParam("dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("dataFim")    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim
    ) {
        return ResponseEntity.ok(contaService.extratoDTO(inicio, fim));
    }

    /** GET /api/contas/abertas – lista só as contas pendentes */
    @GetMapping("/abertas")
    public ResponseEntity<List<ContaDTO>> contasEmAberto() {
        return ResponseEntity.ok(contaService.listarContasEmAbertoDTO());
    }

    /** GET /api/contas/gastos/por-tipo – soma dos pendentes agrupados por tipo */
    @GetMapping("/gastos/por-tipo")
    public ResponseEntity<List<GastosTipoDTO>> gastosPorTipo() {
        return ResponseEntity.ok(contaService.gastosPorTipo());
    }

    /** POST /api/contas – cria nova conta com rateios e retorna DTO */
    @PostMapping
    public ResponseEntity<ContaDTO> createConta(@RequestBody Conta conta) {
        Conta criada = contaService.criarConta(conta);
        ContaDTO dto = new ContaDTO(criada);
        return ResponseEntity.status(201).body(dto);
    }

    /** PUT /api/contas/{id} – atualiza conta existente */
    @PutMapping("/{id}")
    public ResponseEntity<ContaDTO> updateConta(
            @PathVariable Long id,
            @RequestBody Conta conta
    ) {
        try {
            Conta atual = contaService.atualizarConta(id, conta);
            if (atual == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(new ContaDTO(atual));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().build(); // Ou usar um ErrorResponseDTO para detalhes
        }
    }

    /** DELETE /api/contas/{id} – exclui conta */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConta(@PathVariable Long id) {
        contaService.deletarConta(id);
        return ResponseEntity.noContent().build();
    }

    /** POST /api/contas/{id}/replicar – replica uma conta existente */
    @PostMapping("/{id}/replicar")
    public ResponseEntity<ContaDTO> replicateConta(
            @PathVariable Long id,
            @RequestBody Conta contaAlterada
    ) {
        Conta copia = contaService.replicarConta(id, contaAlterada);
        if (copia == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(201).body(new ContaDTO(copia));
    }
}