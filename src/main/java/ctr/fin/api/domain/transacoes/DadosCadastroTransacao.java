package ctr.fin.api.domain.transacoes;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosCadastroTransacao(LocalDate data, BigDecimal valor, TipoTransacao tipo) {


}

