package com.cefet.trab_republica.repositories;

import com.cefet.trab_republica.entities.Rateio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RateioRepository extends JpaRepository<Rateio, Long> {
    // <--- NOVO MÉTODO: Para buscar todos os rateios de uma conta
    List<Rateio> findByContaId(Long contaId);
}