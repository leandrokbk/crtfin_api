package ctr.fin.api.domain.usuario;

public record DadosDetalhamentoUsuario(String login, String senhaAtual) {
    public DadosDetalhamentoUsuario(Usuario usuario) {
        this(usuario.getLogin(), usuario.getSenha());
    }
}
