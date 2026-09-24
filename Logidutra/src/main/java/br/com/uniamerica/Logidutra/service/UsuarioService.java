package br.com.uniamerica.Logidutra.service;

import br.com.uniamerica.Logidutra.controller.UsuarioController;
import br.com.uniamerica.Logidutra.controller.dto.UsuarioRequest;
import br.com.uniamerica.Logidutra.entity.Usuario;
import br.com.uniamerica.Logidutra.enums.Role;
import br.com.uniamerica.Logidutra.enums.StatusOperacional;
import br.com.uniamerica.Logidutra.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RoleService roleService;

    @Transactional
    public Usuario salvar(UsuarioRequest usuarioRequest, Usuario usuarioLogado) {
        roleService.validarRole(usuarioLogado.getId(), Role.ADMIN);

        String nomeLimpo = usuarioRequest.nome().trim();
        if (usuarioRepository.existsByNomeIgnoreCase(nomeLimpo)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um usuário com o nome: " + nomeLimpo);
        }

        log.info("Usuário {} criando novo usuário: {}", usuarioLogado.getNome(), nomeLimpo);

        Usuario usuario = new Usuario();
        usuario.setNome(nomeLimpo);
        usuario.setIdade(usuarioRequest.idade());
        usuario.setRole(usuarioRequest.role());
        usuario.setSenha(usuarioRequest.senha());
        usuario.setStatus(StatusOperacional.DISPONIVEL);

        return this.usuarioRepository.save(usuario);
    }

    public Usuario login(String nome, String senha) {
        String nomeLimpo = nome != null ? nome.trim() : "";
        Usuario usuario = usuarioRepository.findByNomeIgnoreCase(nomeLimpo);
        if (usuario == null) {
            log.warn("Tentativa de login com usuário inexistente: {}", nomeLimpo);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }
        if (!usuario.getSenha().equals(senha)) {
            log.warn("Senha incorreta para o usuário: {}", nomeLimpo);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha incorreta");
        }

        return usuario;
    }

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

        usuario.setSenha(novaSenha);
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
        roleService.validarRole(usuarioLogado.getId(), Role.ADMIN);

        Usuario usuario1 = this.buscarPorId(id);

        usuario1.setNome(usuarioRequest.nome());
        usuario1.setSenha(usuarioRequest.senha());
        usuario1.setIdade(usuarioRequest.idade());
        usuario1.setRole(usuarioRequest.role());

        return this.usuarioRepository.save(usuario1);
    }

    @Transactional
    public Usuario atualizarParcial(long id, UsuarioRequest usuarioRequest, Usuario usuarioLogado) {
        roleService.validarRole(usuarioLogado.getId(), Role.ADMIN);

        Usuario usuario = this.buscarPorId(id);

        if (usuarioRequest.nome() != null) usuario.setNome(usuarioRequest.nome());
        if (usuarioRequest.senha() != null) usuario.setSenha(usuarioRequest.senha());
        if (usuarioRequest.idade() != null) usuario.setIdade(usuarioRequest.idade());
        if (usuarioRequest.role() != null) usuario.setRole(usuarioRequest.role());

        return this.usuarioRepository.save(usuario);

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
        roleService.validarRole(usuarioLogado.getId(), Role.ADMIN);
        log.info("Usuário {} deletando usuário {}", usuarioLogado.getNome(), id);
        this.buscarPorId(id);
        this.usuarioRepository.deleteById(id);
    }

}
