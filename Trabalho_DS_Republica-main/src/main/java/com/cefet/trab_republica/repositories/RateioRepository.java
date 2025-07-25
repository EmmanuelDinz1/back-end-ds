package com.cefet.trab_republica.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cefet.trab_republica.entities.Rateio;

@Repository
public interface RateioRepository extends JpaRepository<Rateio, Long> {
    // Encontra rateios de uma conta específica
    List<Rateio> findByContaId(Long contaId);
}