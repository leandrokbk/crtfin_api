package ctr.fin.api.domain.transacoes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseCurrency {

    @JsonProperty("baseCurrency")
    private String baseCurrency;


}
