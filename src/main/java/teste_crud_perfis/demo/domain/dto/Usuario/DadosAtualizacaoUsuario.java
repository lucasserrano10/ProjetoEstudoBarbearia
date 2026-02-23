package teste_crud_perfis.demo.domain.dto.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DadosAtualizacaoUsuario(
        @NotBlank
        String nome,
        @NotBlank
        @Email
        String email
) {
}
