package ctr.fin.api.domain.transacoes;
import ctr.fin.api.domain.transacoes.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepesitory repository;

    @Transactional
    public Transacao cadastrarTransacao(@Valid DadosCadastroTransacao dados) {
        var transacao = new Transacao(dados);
        repository.save(transacao);
        return transacao;
    }

    public Page<DadosListagemTrasacao> listarTransacoes(Pageable paginacao) {
        return repository.findAll(paginacao).map(DadosListagemTrasacao::new);
    }

    @Transactional
    public Transacao atualizarTransacao(DadosAtualizacaoTransacao dados) {
        var transacao = repository.getReferenceById(dados.id());
        transacao.atualizarInformacoes(dados);
        return transacao;
    }

    @Transactional
    public void excluirTransacao(Long id) {
        repository.deleteById(id);
    }
}

