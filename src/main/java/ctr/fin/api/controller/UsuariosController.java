package ctr.fin.api.controller;

import ctr.fin.api.domain.transacoes.DadosAtualizacaoTransacao;
import ctr.fin.api.domain.transacoes.DadosDetalhamentoTransacao;
import ctr.fin.api.domain.transacoes.DadosListagemTrasacao;
import ctr.fin.api.domain.usuario.*;
import ctr.fin.api.infra.security.SecurityConfigurations;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("usuarios")
@SecurityRequirement(name = "bearer-key")
public class UsuariosController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity cadastrarusuario(@RequestBody DadosCadastroUsuario dados){

        String retornoCadastro = usuarioService.cadastrarUsuario(dados);

        if ("Usuario cadastrado com sucesso" != retornoCadastro) {
            // Retorna com status 409 (Conflito) e a mensagem
            return ResponseEntity.status(409).body(retornoCadastro);
        }

        return ResponseEntity.noContent().build();

    }


    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity inativarUsuario(@PathVariable Long id){
        usuarioService.inativaUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoUsuario> atualizarUsuario(@RequestBody DadosAtualizacaoUsuario dados){
        var usuario = usuarioService.atualizarDadosUsuario(dados);

        return ResponseEntity.ok(new DadosDetalhamentoUsuario(usuario));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemUsuario>>listarUsuarios(Pageable paginacao){
        var page = usuarioService.listaUsuarios(paginacao).map(DadosListagemUsuario::new);
        return ResponseEntity.ok(page);
    }

    @PostMapping("/massivo")
    public String uploadExcelFile(@RequestParam("file") MultipartFile file) throws IOException {
        return usuarioService.processarArquivo(file);

    }


}
