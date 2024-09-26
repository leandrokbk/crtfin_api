package ctr.fin.api.domain.transacoes;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name = "transacoes")
@Entity(name = "Transacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Transacao  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private LocalDate data;
    private BigDecimal valor;
    @Enumerated
    private TipoTransacao tipo;
    public Transacao(DadosCadastroTransacao dados) {

        this.data = dados.data();
        this.valor = dados.valor();
        this.tipo = dados.tipo();
    }

}
