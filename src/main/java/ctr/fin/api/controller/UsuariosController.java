package ctr.fin.api.controller;

import ctr.fin.api.domain.transacoes.DadosListagemTrasacao;
import ctr.fin.api.domain.usuario.*;
import ctr.fin.api.infra.security.SecurityConfigurations;
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
public class UsuariosController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public void cadastrarusuario(@RequestBody DadosCadastroUsuario dados){

        usuarioService.cadastrarUsuario(dados);
    }


    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity inativarUsuario(@PathVariable Long id){
        usuarioService.inativaUsuario(id);
        return ResponseEntity.noContent().build();
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
