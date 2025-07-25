package com.cefet.trab_republica.repositories;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cefet.trab_republica.dto.GastosTipoDTO;
import com.cefet.trab_republica.entities.Conta;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
    List<Conta> findByResponsavelId(Long responsavelId);
    List<Conta> findByDataVencimentoBetween(LocalDate inicio, LocalDate fim);

    @Query("""
      SELECT tc.descricao   AS tipoDescricao,
             SUM(c.valor)   AS total
        FROM Conta c
        JOIN c.tipoConta tc
       WHERE c.situacao = 'PENDENTE'
       GROUP BY tc.descricao
    """)
    List<GastosTipoDTO> totalGastosPorTipo();

    @Query("SELECT c FROM Conta c WHERE c.situacao = 'PENDENTE'")
    List<Conta> findContasEmAberto();
}