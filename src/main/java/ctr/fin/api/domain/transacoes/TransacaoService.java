package ctr.fin.api.domain.transacoes;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import jakarta.annotation.PostConstruct;

import java.util.Map;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository repository;

    private final WebClient.Builder webClientBuilder;

    @Value("${exchangerates.api.url}")
    private String apiUrl;

    @Value("${exchangerates.api.key}")
    private String apiKey;

    @Value("${exchangerates.api.moedaBase}")
    private String moedaBase;

    // Construtor com injeção de dependência
    @Autowired
    public TransacaoService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

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


    @PostConstruct
    public void init() {
        System.out.println("API URL: " + apiUrl);
        System.out.println("API Key: " + apiKey);
        System.out.println("API Key: " + moedaBase);
    }
    // Método que faz a chamada à API de câmbio
    public ResponseEntity<String> getExchangeRates(String baseCurrency) {
        // Inicializando o WebClient dentro do método
        RestClient webClient = RestClient.builder()
                .baseUrl(apiUrl)
                .build();

        // Fazendo a requisição à API
        ResponseEntity<Map> responseEntity = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/latest")
                        .queryParam("access_key", apiKey)
                        .queryParam("base", moedaBase)
                        .build())
                .retrieve()
                .toEntity(Map.class);  // Pegando a resposta como um Map (JSON)

        // Extrair o valor da chave "BRL"
        if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
            Map<String, Object> responseBody = responseEntity.getBody();
            Map<String, Double> rates = (Map<String, Double>) responseBody.get("rates");
            Double rate = rates.get(baseCurrency);
           // Double euroRate = rates.get(moedaBase);

            if (rate != null) {
                return ResponseEntity.ok(rate.toString());
            } else {
                return ResponseEntity.badRequest().body("Taxa não encontrada");
            }
        } else {
            return ResponseEntity.status(responseEntity.getStatusCode()).body("Erro na requisição");
        }
    }
}
