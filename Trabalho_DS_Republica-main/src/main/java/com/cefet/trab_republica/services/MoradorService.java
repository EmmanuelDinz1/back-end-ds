package com.cefet.trab_republica.services;

import java.util.List;
import java.util.Optional; // Importe Optional
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cefet.trab_republica.dto.SaldoMoradorDTO;
import com.cefet.trab_republica.entities.Morador;
import com.cefet.trab_republica.repositories.MoradorRepository;

@Service
public class MoradorService {

    @Autowired
    private MoradorRepository moradorRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Morador cadastrarMorador(Morador morador) {
        String senhaHash = passwordEncoder.encode(morador.getSenha());
        morador.setSenha(senhaHash);
        return moradorRepository.save(morador);
    }

    public List<Morador> listarMoradores() {
        return moradorRepository.findAll();
    }

    public List<SaldoMoradorDTO> getSaldos() {
        return moradorRepository.calcularSaldoMoradores();
    }

    public Morador buscarMorador(Long id) {
        return moradorRepository.findById(id).orElse(null);
    }

    public Morador atualizarMorador(Long id, Morador dadosAtualizados) {
        Morador existente = moradorRepository.findById(id).orElse(null);
        if (existente == null) {
            return null;
        }
        existente.setNome(dadosAtualizados.getNome());
        existente.setEmail(dadosAtualizados.getEmail());
        if (dadosAtualizados.getSenha() != null && !dadosAtualizados.getSenha().isEmpty()) {
            String senhaHash = passwordEncoder.encode(dadosAtualizados.getSenha());
            existente.setSenha(senhaHash);
        }
        return moradorRepository.save(existente);
    }

    public void deletarMorador(Long id) {
        moradorRepository.deleteById(id);
    }

    public Optional<Morador> findByEmail(String email) { // Agora retorna Optional
        return moradorRepository.findByEmail(email);
    }

    public Morador autenticarEObterMorador(String email, String senha) {
        Optional<Morador> moradorOptional = moradorRepository.findByEmail(email);
        if (moradorOptional.isEmpty()) {
            return null; // Usuário não encontrado
        }
        Morador m = moradorOptional.get(); // Obtém o Morador de dentro do Optional
        if (passwordEncoder.matches(senha, m.getSenha())) {
            return m; // Credenciais válidas, retorna o morador
        }
        return null; // Senha inválida
    }

    public void recuperarSenha(String email) {
        // TODO: implementar envio de token ou nova senha
    }
}