package ctr.fin.api.domain.usuario;

public record DadosAtualizacaoUsuario( Long id, String login, String senhaAtual, String novaSenha, String ativo) {
}
