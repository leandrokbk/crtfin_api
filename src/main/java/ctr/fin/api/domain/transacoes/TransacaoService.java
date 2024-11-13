package ctr.fin.api.domain.transacoes;

import jakarta.validation.Valid;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import jakarta.annotation.PostConstruct;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
        if (!"BRL".equals(transacao.getMoeda())){
            //BigDecimal cotacao = getExchangeRates(transacao.getMoeda()).getBody();
             RatesResult ratesResult = getExchangeRates2(transacao.getMoeda());
          // transacao.setValor_convertido(transacao.getValor().multiply(cotacao));
            transacao.setValor_convertido(transacao.getValor().multiply(ratesResult.rate()).multiply(ratesResult.rateBRL())
                    .setScale(2, RoundingMode.HALF_UP));

            repository.save(transacao);
            return transacao;
        }


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
    public ResponseEntity<BigDecimal> getExchangeRates(String baseCurrency) {
        // Inicializando o WebClient dentro do método
        RestClient webClient = RestClient.builder()
                .baseUrl(apiUrl)
                .build();

        // Fazendo a requisição à API
        ResponseEntity<Map> responseEntity = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/latest")
                        .queryParam("access_key", apiKey)
                        .queryParam("base", baseCurrency)
                        .build())
                .retrieve()
                .toEntity(Map.class);  // Pegando a resposta como um Map (JSON)

        // Extrair o valor da chave "BRL"
        if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
            Map<String, Object> responseBody = responseEntity.getBody();
            Map<String, Object> rates = (Map<String, Object>) responseBody.get("rates");
            BigDecimal rate = new BigDecimal(rates.get(moedaBase).toString());
            // BigDecimal rate = rates.get(moedaBase);
           // Double euroRate = rates.get(moedaBase);

            if (rate != null) {
                return ResponseEntity.ok(rate);
            } else {
                return ResponseEntity.badRequest().body(rate);
            }
        } else {
            return (ResponseEntity<BigDecimal>) ResponseEntity.status(responseEntity.getStatusCode());
        }
    }

    // Método que faz a chamada à API de câmbio
    public RatesResult getExchangeRates2(String baseCurrency) {
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

        // Verificando se a resposta é bem-sucedida e contém um corpo
        if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
            Map<String, Object> responseBody = responseEntity.getBody();
            Map<String, Object> rates = (Map<String, Object>) responseBody.get("rates");

            // Extrair as taxas para as moedas especificadas
            BigDecimal rate = new BigDecimal(rates.get(baseCurrency).toString());
            BigDecimal rateBRL = new BigDecimal(rates.get("BRL").toString());

            // Retornar um novo RatesResult com os valores
            return new RatesResult(rate, rateBRL);
        }

        // Caso a resposta não seja bem-sucedida, retornar um RatesResult vazio ou com valores padrão
        return new RatesResult(BigDecimal.ZERO, BigDecimal.ZERO);
    }

    public ResponseEntity<InputStreamResource> gerarRelatoriodDataTransacao(LocalDate startDate, LocalDate endDate) {
        // Buscar transações no repositório
        List<Transacao> transacoes = repository.findByDataTransacaoBetween(startDate, endDate);

        // Criar um Workbook para o Excel
        try (Workbook workbook = new XSSFWorkbook()) {

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));

            Sheet sheet = workbook.createSheet("Relatorio transações por data" );

            // Criar cabeçalho do relatório
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Data");
            headerRow.createCell(2).setCellValue("Valor");
            headerRow.createCell(3).setCellValue("Tipo");
            headerRow.createCell(4).setCellValue("Moeda");
            headerRow.createCell(5).setCellValue("Valor Convertido");

            // Preencher os dados
            int rowNum = 1;
            for (Transacao transacao : transacoes) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(transacao.getId());
                row.createCell(1).setCellValue(transacao.getData().toString());
                row.createCell(2).setCellValue(transacao.getValor().toString());
                row.createCell(3).setCellValue(transacao.getTipo().toString());
                row.createCell(4).setCellValue(transacao.getMoeda());
                row.createCell(5).setCellValue(transacao.getValor_convertido().toString());
            }

            // Escrever o Workbook em um ByteArrayOutputStream
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            // Retornar o arquivo como um recurso de entrada para download
            ByteArrayResource resource = new ByteArrayResource(out.toByteArray());

            String filename = "RelatorioTransacoes_" + timestamp + ".xlsx";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(resource.contentLength())
                    .body(new InputStreamResource(resource.getInputStream()));
        } catch (IOException e) {
            // Em caso de erro, retornar uma resposta de erro
            return ResponseEntity.internalServerError().build();
        }
    }

}
