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

    @PostMapping
    @Transactional
    public ResponseEntity cadastroTransacao (@RequestBody @Valid DadosCadastroTransacao dados, UriComponentsBuilder uriBuilder){

        var transacao = new Transacao(dados);
        repository.save(transacao);

        var uri = uriBuilder.path("/transacao/{id}").buildAndExpand(transacao.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoTransacao(transacao));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemTrasacao> >listar(Pageable paginacao){
        var page = repository.findAll(paginacao).map(DadosListagemTrasacao::new);
        return ResponseEntity.ok(page);
    }


    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody DadosAtualizacaoTransacao dados){
        var transacao = repository.getReferenceById(dados.id());
        transacao.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoTransacao(transacao));
    }
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluirTransacao(@PathVariable Long id){

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}
