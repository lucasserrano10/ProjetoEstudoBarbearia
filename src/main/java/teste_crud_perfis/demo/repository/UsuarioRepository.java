package teste_crud_perfis.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import teste_crud_perfis.demo.domain.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
}