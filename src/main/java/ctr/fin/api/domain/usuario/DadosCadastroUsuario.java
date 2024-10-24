package ctr.fin.api.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


public record DadosCadastroUsuario(
        @NotBlank @Email
        String login,
        @NotBlank
        String senha) {


}

