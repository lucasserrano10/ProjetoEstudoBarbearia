package teste_crud_perfis.demo.domain.dto.Usuario;

import teste_crud_perfis.demo.domain.Usuario;

public record DadosListagemUsuario(
        Long id,
        String nome,
        String email
) {

    public DadosListagemUsuario(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail()
        );
    }
}