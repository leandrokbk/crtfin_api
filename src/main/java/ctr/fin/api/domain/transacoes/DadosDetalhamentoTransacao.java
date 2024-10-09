package ctr.fin.api.domain.transacoes;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosDetalhamentoTransacao(Long id, LocalDate data, BigDecimal valor,TipoTransacao tipo) {

    public DadosDetalhamentoTransacao(Transacao transacao){
        this(transacao.getId(),transacao.getData(),transacao.getValor(),transacao.getTipo());
    }


}
