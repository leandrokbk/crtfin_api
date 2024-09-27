package ctr.fin.api.domain.transacoes;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosAtualizacaoTransacao(
        @NotNull
        Long id,
        BigDecimal valor,
        TipoTransacao tipo) {

}
