package com.cefet.trab_republica.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cefet.trab_republica.entities.HistoricoConta;

@Repository
public interface HistoricoContaRepository extends JpaRepository<HistoricoConta, Long> {
    List<HistoricoConta> findByContaId(Long contaId);
}