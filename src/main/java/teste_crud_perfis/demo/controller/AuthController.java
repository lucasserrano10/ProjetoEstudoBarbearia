package teste_crud_perfis.demo.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import teste_crud_perfis.demo.domain.Usuario;
import teste_crud_perfis.demo.domain.dto.Usuario.DadosLogin;
import teste_crud_perfis.demo.domain.dto.Usuario.DadosRegistro;
import teste_crud_perfis.demo.domain.dto.Usuario.MensagemResponse;
import teste_crud_perfis.demo.domain.dto.Usuario.TokenResponse;
import teste_crud_perfis.demo.repository.UsuarioRepository;
import teste_crud_perfis.demo.security.JwtService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(UsuarioRepository repository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<MensagemResponse> register(
            @RequestBody @Valid DadosRegistro dados) {

        if (repository.existsByEmail(dados.email())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email já cadastrado"
            );
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dados.nome());
        usuario.setEmail(dados.email());
        usuario.setSenha(passwordEncoder.encode(dados.senha()));

        repository.save(usuario);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new MensagemResponse("Usuário cadastrado com sucesso"));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @RequestBody @Valid DadosLogin dados) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dados.email(),
                        dados.senha()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.gerarToken(userDetails);

        return ResponseEntity.ok(new TokenResponse(token));
    }
}