package teste_crud_perfis.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import teste_crud_perfis.demo.domain.Usuario;
import teste_crud_perfis.demo.domain.dto.Usuario.DadosAtualizacaoUsuario;
import teste_crud_perfis.demo.domain.dto.Usuario.DadosCadastroUsuario;
import teste_crud_perfis.demo.domain.dto.Usuario.DadosListagemUsuario;
import teste_crud_perfis.demo.repository.UsuarioRepository;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder){
        this.repository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<DadosListagemUsuario> criar(@RequestBody DadosCadastroUsuario dados, UriComponentsBuilder uriBuilder) {
        String senhaCripto = passwordEncoder.encode(dados.senha());

        var usuario = new Usuario();
        usuario.setNome(dados.nome());
        usuario.setEmail(dados.email());
        usuario.setSenha(senhaCripto);

        usuario = repository.save(usuario);

        var uri = uriBuilder.path("/usuarios/{id}")
                .buildAndExpand(usuario.getId())
                .toUri();

        return ResponseEntity
                .created(uri)
                .body(new DadosListagemUsuario(usuario));
    }

    @GetMapping
    public ResponseEntity<List<DadosListagemUsuario>> listar() {
        var usuarios = repository.findAll();

        var listaDto = usuarios.stream()
                .map(DadosListagemUsuario::new)
                .toList();

        return ResponseEntity.ok(listaDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosListagemUsuario> buscar(@PathVariable Long id) {
        var usuario = repository.findById(id).orElseThrow();
        return ResponseEntity.ok(new DadosListagemUsuario(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosListagemUsuario> atualizar(
            @PathVariable Long id,
            @RequestBody DadosAtualizacaoUsuario dados) {

        var usuario = repository.findById(id)
                .orElseThrow();

        usuario.setNome(dados.nome());
        usuario.setEmail(dados.email());

        repository.save(usuario);

        return ResponseEntity.ok(new DadosListagemUsuario(usuario));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}