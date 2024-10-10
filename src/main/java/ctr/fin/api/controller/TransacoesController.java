package ctr.fin.api.controller;

import ctr.fin.api.domain.transacoes.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("transacao")
public class TransacoesController {

    @Autowired
    public TransacaoRepesitory repository;

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

}
