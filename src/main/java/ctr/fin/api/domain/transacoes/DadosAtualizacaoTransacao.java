package ctr.fin.api.domain.transacoes;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosAtualizacaoTransacao(Long id, BigDecimal valor, TipoTransacao tipo) {

}
