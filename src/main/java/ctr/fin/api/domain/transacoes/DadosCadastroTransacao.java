package ctr.fin.api.domain.transacoes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosCadastroTransacao(


        @NotNull
        LocalDate data,
        @NotNull
        BigDecimal valor,
        @NotNull
        TipoTransacao tipo) {


}

