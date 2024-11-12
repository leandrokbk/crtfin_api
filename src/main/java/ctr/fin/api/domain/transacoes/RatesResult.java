package ctr.fin.api.domain.transacoes;

import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

public record RatesResult(BigDecimal rate, BigDecimal rateBRL) {

/*    public RatesResult(ResponseEntity<RatesResult> exchangeRates2) {
        this.rate = exchangeRates2.getBody().rate();
        this.rateBRL = exchangeRates2.getBody().rateBRL();
    }*/
}
