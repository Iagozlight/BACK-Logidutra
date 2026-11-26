package br.com.uniamerica.logidutra.service;

import br.com.uniamerica.logidutra.controller.dto.UsuarioRequest;
import br.com.uniamerica.logidutra.entity.Usuario;
import br.com.uniamerica.logidutra.enums.Role;
import br.com.uniamerica.logidutra.enums.StatusOperacional;
import br.com.uniamerica.logidutra.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import br.com.uniamerica.logidutra.controller.dto.AlterarSenhaRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario salvar(UsuarioRequest usuarioRequest, Usuario usuarioLogado) {
        String nomeLimpo = usuarioRequest.nome().trim();
        String loginLimpo = usuarioRequest.login().trim();

        if (usuarioRepository.existsByNomeIgnoreCase(nomeLimpo)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um usuário com o nome: " + nomeLimpo);
        }
        if (usuarioRepository.existsByLogin(loginLimpo)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um usuário com o login: " + loginLimpo);
        }

        log.info("Usuário {} criando novo usuário: {}", usuarioLogado.getNome(), nomeLimpo);

        Usuario usuario = new Usuario();
        usuario.setNome(nomeLimpo);
        usuario.setLogin(loginLimpo);
        usuario.setIdade(usuarioRequest.idade());
        usuario.setRole(usuarioRequest.role());
        usuario.setSenha(passwordEncoder.encode(usuarioRequest.senha()));
        usuario.setStatus(StatusOperacional.DISPONIVEL);

        return this.usuarioRepository.save(usuario);
    }

//    public Usuario login(String nome, String senha) {
//        String nomeLimpo = nome != null ? nome.trim() : "";
//        Usuario usuario = usuarioRepository.findByNomeIgnoreCase(nomeLimpo);
//        if (usuario == null) {
//            log.warn("Tentativa de login com usuário inexistente: {}", nomeLimpo);
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
//        }
//        if (!usuario.getSenha().equals(senha)) {
//            log.warn("Senha incorreta para o usuário: {}", nomeLimpo);
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha incorreta");
//        }
//
//        return usuario;
//    }

    @Transactional
    public void redefinirSenha(String nome, String novaSenha) {
        String nomeLimpo = nome != null ? nome.trim() : "";
        Usuario usuario = usuarioRepository.findByNomeIgnoreCase(nomeLimpo);
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado.");
        }
        if (usuario.getRole() == Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Administradores não podem redefinir a senha por este caminho.");
        }

        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);
        log.info("Senha redefinida com sucesso para o usuário: {}", nomeLimpo);
    }


    public List<Usuario> listar() {
        return this.usuarioRepository.findAll();
    }

    public Usuario buscarPorId(long id) {
        return this.usuarioRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Usuario " + id + " Não encontrado")
                );
    }

    @Transactional
    public Usuario atualizar(long id, UsuarioRequest usuarioRequest, Usuario usuarioLogado) {
        Usuario usuario = buscarPorId(id);

        String nomeLimpo = usuarioRequest.nome().trim();
        String loginLimpo = usuarioRequest.login().trim();

        if (usuarioRepository.existsByNomeIgnoreCaseAndIdNot(nomeLimpo, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um usuário com o nome: " + nomeLimpo);
        }
        if (usuarioRepository.existsByLoginAndIdNot(loginLimpo, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um usuário com o login: " + loginLimpo);
        }

        log.info("Usuário {} atualizando usuário {}", usuarioLogado.getNome(), id);

        usuario.setNome(nomeLimpo);
        usuario.setLogin(loginLimpo);
        usuario.setSenha(passwordEncoder.encode(usuarioRequest.senha()));
        usuario.setIdade(usuarioRequest.idade());
        usuario.setRole(usuarioRequest.role());

        return usuarioRepository.save(usuario);

    }

    @Transactional
    public Usuario atualizarParcial(long id, UsuarioRequest usuarioRequest, Usuario usuarioLogado) {
        Usuario usuario = buscarPorId(id);

        log.info("Usuário {} atualizando parcialmente o usuário {}", usuarioLogado.getNome(), id);

        if (usuarioRequest.nome() != null && !usuarioRequest.nome().isBlank()) {
            String nomeLimpo = usuarioRequest.nome().trim();
            if (usuarioRepository.existsByNomeIgnoreCaseAndIdNot(nomeLimpo, id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um usuário com o nome: " + nomeLimpo);
            }
            usuario.setNome(nomeLimpo);
        }
        if (usuarioRequest.login() != null && !usuarioRequest.login().isBlank()) {
            String loginLimpo = usuarioRequest.login().trim();
            if (usuarioRepository.existsByLoginAndIdNot(loginLimpo, id)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um usuário com o login: " + loginLimpo);
            }
            usuario.setLogin(loginLimpo);
        }
        if (usuarioRequest.senha() != null && !usuarioRequest.senha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(usuarioRequest.senha()));
        }
        if (usuarioRequest.idade() != null) usuario.setIdade(usuarioRequest.idade());
        if (usuarioRequest.role() != null) usuario.setRole(usuarioRequest.role());

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario marcarEmRota(long id) {
        Usuario usuario = this.buscarPorId(id);

        if (usuario.getStatus() == StatusOperacional.EM_ROTA) {
            log.warn("Tentativa de alocar motorista {} que já está em rota", id);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Motorista já está em rota");
        }

        usuario.setStatus(StatusOperacional.EM_ROTA);
        log.info("Usuário {} marcado como EM_ROTA", id);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario marcarDisponivel(long id) {
        Usuario usuario = this.buscarPorId(id);
        usuario.setStatus(StatusOperacional.DISPONIVEL);
        log.info("Usuário {} marcado como DISPONIVEL", id);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void deletarPorId(long id, Usuario usuarioLogado) {
        log.info("Usuário {} deletando usuário {}", usuarioLogado.getNome(), id);
        buscarPorId(id);
        usuarioRepository.deleteById(id);
        if (id == usuarioLogado.getId()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Você não pode excluir a própria conta");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    @Transactional
    public void alterarMinhaSenha(Long id, AlterarSenhaRequest request) {
        Usuario usuario = buscarPorId(id);

        if (!passwordEncoder.matches(request.senhaAtual(), usuario.getSenha())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha atual incorreta");
        }
        usuario.setSenha(passwordEncoder.encode(request.novaSenha()));
        usuarioRepository.save(usuario);
        log.info("Usuario {} alterou a propria senha", usuario.getLogin());
    }

}
