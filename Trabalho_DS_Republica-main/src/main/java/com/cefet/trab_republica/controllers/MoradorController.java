package com.cefet.trab_republica.controllers;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cefet.trab_republica.dto.SaldoMoradorDTO;
import com.cefet.trab_republica.entities.Morador;
import com.cefet.trab_republica.services.MoradorService;
import com.cefet.trab_republica.dto.LoginResponseDTO;
import com.cefet.trab_republica.services.TokenService;

@RestController
@RequestMapping("/api/moradores")
public class MoradorController {

    @Autowired
    private MoradorService moradorService;
    @Autowired
    private TokenService tokenService;

    @GetMapping
    public ResponseEntity<List<Morador>> getAllMoradores() {
        return ResponseEntity.ok(moradorService.listarMoradores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Morador> getMoradorById(@PathVariable Long id) {
        Morador morador = moradorService.buscarMorador(id);
        if (morador == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(morador);
    }

    @GetMapping("/saldos")
    public ResponseEntity<List<SaldoMoradorDTO>> getSaldos() {
        return ResponseEntity.ok(moradorService.getSaldos());
    }

    @PostMapping
    public ResponseEntity<Morador> createMorador(@RequestBody Morador morador) {
        Morador criado = moradorService.cadastrarMorador(morador);
        return ResponseEntity.status(201).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Morador> updateMorador(@PathVariable Long id, @RequestBody Morador morador) {
        Morador atualizado = moradorService.atualizarMorador(id, morador);
        if (atualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMorador(@PathVariable Long id) {
        moradorService.deletarMorador(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/auth")
    public ResponseEntity<LoginResponseDTO> authenticate(@RequestBody Map<String, String> creds) {
        String email = creds.get("email");
        String senha = creds.get("senha");

        Morador moradorAutenticado = moradorService.autenticarEObterMorador(email, senha);

        if (moradorAutenticado == null) {
            return ResponseEntity.status(401).body(new LoginResponseDTO("Credenciais inválidas", null, null, null));
        }

        String token = tokenService.generateToken(moradorAutenticado);

        return ResponseEntity.ok(new LoginResponseDTO("Autenticado com sucesso", moradorAutenticado.getId(), moradorAutenticado.getNome(), token));
    }

    @PostMapping("/recuperar")
    public ResponseEntity<String> recuperarSenha(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        moradorService.recuperarSenha(email);
        return ResponseEntity.ok("Instruções de recuperação enviadas para o email, se cadastrado.");
    }
}