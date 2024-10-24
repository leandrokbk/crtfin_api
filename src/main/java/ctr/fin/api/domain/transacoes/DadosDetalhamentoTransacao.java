package ctr.fin.api.domain.transacoes;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosDetalhamentoTransacao(Long id, LocalDate data, BigDecimal valor,TipoTransacao tipo, String moeda, BigDecimal valor_convertido ) {

    public DadosDetalhamentoTransacao(Transacao transacao){
        this(transacao.getId(),transacao.getData(),transacao.getValor(),transacao.getTipo(), transacao.getMoeda(), transacao.getValor_convertido());
    }


}
