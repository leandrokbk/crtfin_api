package ctr.fin.api.domain.usuario;

import ctr.fin.api.domain.transacoes.Transacao;

public record DadosListagemUsuario(Long id, String login, Boolean ativo) {

    public DadosListagemUsuario(Usuario usuario){
        this(usuario.getId(), usuario.getLogin(), usuario.getAtivo());
    }
}
