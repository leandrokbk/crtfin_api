package ctr.fin.api.controller;

import ctr.fin.api.domain.usuario.Usuario;
import ctr.fin.api.domain.usuario.UsuarioRepository;
import ctr.fin.api.domain.usuario.DadosCadastroUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("usuarios")
public class UsuariosController {


    @Autowired
    private UsuarioRepository repository;
    @PostMapping
    public void cadastrarusuario(@RequestBody DadosCadastroUsuario dados){
        repository.save(new Usuario(dados) );
    }
}
