package com.cefet.trab_republica.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cefet.trab_republica.entities.TipoConta;
import com.cefet.trab_republica.services.TipoContaService;

@RestController
@RequestMapping("/api/tipos")
public class TipoContaController {

    @Autowired
    private TipoContaService tipoContaService;

    @GetMapping
    public ResponseEntity<List<TipoConta>> getAllTipos() {
        return ResponseEntity.ok(tipoContaService.listarTiposConta());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoConta> getTipoById(@PathVariable Long id) {
        TipoConta tipo = tipoContaService.buscarTipoConta(id);
        if (tipo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tipo);
    }

    @PostMapping
    public ResponseEntity<TipoConta> createTipo(@RequestBody TipoConta tipoConta) {
        TipoConta criado = tipoContaService.criarTipoConta(tipoConta);
        return ResponseEntity.status(201).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoConta> updateTipo(@PathVariable Long id, @RequestBody TipoConta tipoConta) {
        TipoConta atualizado = tipoContaService.atualizarTipoConta(id, tipoConta);
        if (atualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipo(@PathVariable Long id) {
        tipoContaService.deletarTipoConta(id);
        return ResponseEntity.noContent().build();
    }
}