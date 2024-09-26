package ctr.fin.api.domain.transacoes;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosListagemTrasacao( Long id,LocalDate data, BigDecimal valor, TipoTransacao tipo) {

    public DadosListagemTrasacao(Transacao transacao){
        this(transacao.getId(), transacao.getData(), transacao.getValor(), transacao.getTipo());
    }
}
