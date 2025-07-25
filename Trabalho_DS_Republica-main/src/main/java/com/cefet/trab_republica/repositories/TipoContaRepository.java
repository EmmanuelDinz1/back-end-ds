package com.cefet.trab_republica.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cefet.trab_republica.entities.TipoConta;

@Repository
public interface TipoContaRepository extends JpaRepository<TipoConta, Long> {
    // Nenhum método adicional necessário por enquanto
}