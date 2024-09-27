package ctr.fin.api.controller;

import ctr.fin.api.domain.transacoes.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("transacao")
public class TransacoesController {

    @Autowired
    public TransacaoRepesitory repository;

    @PostMapping
    @Transactional
    public void  cadastroTransacao (@RequestBody DadosCadastroTransacao dados){
        repository.save(new Transacao(dados) );
    }

    @GetMapping
    public Page<DadosListagemTrasacao> listar(Pageable paginacao){
        return repository.findAll(paginacao).map(DadosListagemTrasacao::new);
    }


    @PutMapping
    @Transactional
    public void atualizar(@RequestBody DadosAtualizacaoTransacao dados){
        var transacao = repository.getReferenceById(dados.id());
        transacao.atualizarInformacoes(dados);
    }
    @DeleteMapping("/{id}")
    @Transactional
    public void excluirTransacao(@PathVariable Long id){
        repository.deleteById(id);
    }

}
