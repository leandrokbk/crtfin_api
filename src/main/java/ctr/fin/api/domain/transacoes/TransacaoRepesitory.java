package ctr.fin.api.domain.transacoes;

import ctr.fin.api.domain.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransacaoRepesitory extends JpaRepository<Transacao, Long> {
}
