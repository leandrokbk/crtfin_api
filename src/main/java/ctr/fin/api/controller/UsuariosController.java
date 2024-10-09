package ctr.fin.api.controller;

import ctr.fin.api.domain.transacoes.DadosListagemTrasacao;
import ctr.fin.api.domain.usuario.DadosListagemUsuario;
import ctr.fin.api.domain.usuario.Usuario;
import ctr.fin.api.domain.usuario.UsuarioRepository;
import ctr.fin.api.domain.usuario.DadosCadastroUsuario;
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
    @PostMapping
    public void cadastrarusuario(@RequestBody DadosCadastroUsuario dados){

        Usuario usuario = new Usuario(dados);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        repository.save(usuario);
    }



    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity inativarUsuario(@PathVariable Long id){
        var usuario = repository.getReferenceById(id);
        usuario.inativar();

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemUsuario>>listarUsuarios(Pageable paginacao){
        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemUsuario::new);
        return ResponseEntity.ok(page);
    }

    @PostMapping("/massivo")
    public String uploadExcelFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return "O arquivo está vazio.";
        }

        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            // Acessando a primeira planilha
            Sheet sheet = workbook.getSheetAt(0);

            // Iterando sobre as linhas da planilha
            for (Row row : sheet) {
                // Ignorando o cabeçalho (se houver)
                if (row.getRowNum() == 0) {
                    continue;
                }
                Usuario usuario = new Usuario();
                // Obtendo as 3 colunas
                usuario.setLogin(row.getCell(0).getStringCellValue());

                usuario.setSenha(passwordEncoder.encode(row.getCell(1).getStringCellValue()));

                if ("ATIVO".equalsIgnoreCase(row.getCell(2).getStringCellValue())) {
                    usuario.setAtivo(true);
                }else {
                    usuario.setAtivo(false);
                }

                // Salvando os dados no banco de dados

                repository.save(usuario);
            }
        }

        return "Arquivo processado e dados salvos com sucesso!";
    }


}
