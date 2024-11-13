package ctr.fin.api.domain.transacoes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    @Query("""
            SELECT t FROM Transacao t
            WHERE t.data BETWEEN :startDate AND :endDate
            """)
    List<Transacao> findByDataTransacaoBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
