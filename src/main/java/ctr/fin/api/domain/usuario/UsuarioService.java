package ctr.fin.api.domain.usuario;


import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;


@Service
public class UsuarioService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository repository;



    public String cadastrarUsuario(DadosCadastroUsuario dados){

       // private Boolean verificarUsuario;
        Usuario usuario = new Usuario(dados);

        var verificarUsuario = repository.findByLogin(usuario.getLogin());
        if (verificarUsuario != null) {
            return "Usuario " + usuario.getLogin() + " já cadastrado";
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        repository.save(usuario);
        return "Usuario cadastrado com sucesso";
    }

    @Transactional
    public void inativaUsuario(Long id) {
        var usuario = repository.getReferenceById(id);
        usuario.inativar();

    }

    public Page<Usuario> listaUsuarios(Pageable paginacao) {
        return repository.findAllByAtivoTrue(paginacao);
    }

    @Transactional
    public String processarArquivo(MultipartFile file) throws IOException {


        var validaExcel = ValidaPlanilha.isExcelFileValid(file);

        if (!validaExcel) {
            return "O arquivo Excel está vazio!.";
        }

        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Ignorar cabeçalho
                }

                Usuario usuario = new Usuario();
                usuario.setLogin(row.getCell(0).getStringCellValue());
                usuario.setSenha(passwordEncoder.encode(row.getCell(1).getStringCellValue()));





                if ("ATIVO".equalsIgnoreCase(row.getCell(2).getStringCellValue())) {
                    usuario.setAtivo(true);
                } else {
                    usuario.setAtivo(false);
                }

                //String resultado = cadastrarUsuario(usuario);
                repository.save(usuario);
            }
        }

        return "Usuários cadastrados com sucesso!";
    }




}
