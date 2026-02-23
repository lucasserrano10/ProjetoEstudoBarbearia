package teste_crud_perfis.demo.domain.dto.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DadosRegistro(
        @NotBlank String nome,
        @Email @NotBlank String email,
        @NotBlank String senha
) {}