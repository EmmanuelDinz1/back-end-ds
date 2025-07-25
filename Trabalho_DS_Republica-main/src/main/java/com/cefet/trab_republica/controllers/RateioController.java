package com.cefet.trab_republica.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cefet.trab_republica.entities.Rateio;
import com.cefet.trab_republica.services.RateioService;

@RestController
@RequestMapping("/api/rateios")
public class RateioController {

    @Autowired
    private RateioService rateioService;

    @GetMapping
    public ResponseEntity<List<Rateio>> getAllRateios() {
        return ResponseEntity.ok(rateioService.listarRateios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rateio> getRateioById(@PathVariable Long id) {
        Rateio rateio = rateioService.buscarRateio(id);
        if (rateio == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(rateio);
    }

    @PostMapping
    public ResponseEntity<Rateio> createRateio(@RequestBody Rateio rateio) {
        Rateio criado = rateioService.criarRateio(rateio);
        return ResponseEntity.status(201).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rateio> updateRateio(@PathVariable Long id, @RequestBody Rateio rateio) {
        Rateio atualizado = rateioService.atualizarRateio(id, rateio);
        if (atualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRateio(@PathVariable Long id) {
        rateioService.deletarRateio(id);
        return ResponseEntity.noContent().build();
    }
}