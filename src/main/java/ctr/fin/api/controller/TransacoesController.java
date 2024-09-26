package ctr.fin.api.controller;

import ctr.fin.api.domain.transacoes.DadosCadastroTransacao;
import ctr.fin.api.domain.transacoes.Transacao;
import ctr.fin.api.domain.transacoes.TransacaoRepesitory;
import ctr.fin.api.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("transacao")
public class TransacoesController {

    @Autowired
    public TransacaoRepesitory repository;

    @PostMapping
    public void  cadastroTransacao (@RequestBody DadosCadastroTransacao dados){
        repository.save(new Transacao(dados) );
    }
}
