package ctr.fin.api.controller;

import ctr.fin.api.domain.transacoes.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("transacao")
@SecurityRequirement(name = "bearer-key")
public class TransacoesController {

    @Autowired
    public TransacaoRepository repository;

    @Autowired
    private TransacaoService transacaoService;

    @PostMapping
    @Transactional
    public ResponseEntity <DadosDetalhamentoTransacao>cadastroTransacao (@RequestBody @Valid DadosCadastroTransacao dados, UriComponentsBuilder uriBuilder){
        var transacao = transacaoService.cadastrarTransacao(dados);
        var uri = uriBuilder.path("/transacao/{id}").buildAndExpand(transacao.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoTransacao(transacao));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemTrasacao> >listar(Pageable paginacao){
        var page = transacaoService.listarTransacoes(paginacao);

        return ResponseEntity.ok(page);
    }


    @PutMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoTransacao> atualizar(@RequestBody DadosAtualizacaoTransacao dados){
        var transacao = transacaoService.atualizarTransacao(dados);

        return ResponseEntity.ok(new DadosDetalhamentoTransacao(transacao));
    }
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluirTransacao(@PathVariable Long id){
        transacaoService.excluirTransacao(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cotacao")
    public ResponseEntity<BigDecimal> getExchangeRates(@RequestBody BaseCurrency baseCurrency) {


        String moeda = baseCurrency.getBaseCurrency();

        return  transacaoService.getExchangeRates(moeda);





    }
    @GetMapping("/cotacoes")
    public ResponseEntity<RatesResult> buscaCotacoes(@RequestBody BaseCurrency baseCurrency) {


        String moeda = baseCurrency.getBaseCurrency();


        RatesResult ratesresult = transacaoService.getExchangeRates2(moeda);

        return  ResponseEntity.ok(ratesresult);

    }

    @PostMapping("/relatorio")
    public ResponseEntity<InputStreamResource> gerarRelatorioData(@RequestBody RelatorioData relatorioData) {


        return transacaoService.gerarRelatoriodDataTransacao(relatorioData.dataInicio(), relatorioData.dataFim());
    }


}
