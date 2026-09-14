package br.com.uniamerica.Logidutra.service;

import br.com.uniamerica.Logidutra.controller.UsuarioController;
import br.com.uniamerica.Logidutra.controller.dto.UsuarioRequest;
import br.com.uniamerica.Logidutra.entity.Usuario;
import br.com.uniamerica.Logidutra.enums.Role;
import br.com.uniamerica.Logidutra.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public void validarRole(Usuario usuarioLogado, Role roleExigida) {
        if (usuarioLogado.getRole() != roleExigida) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Usuário não tem a permissão necessária para executar essa ação"
            );
        }
    }

    public Usuario salvar(UsuarioRequest usuarioRequest, Usuario usuarioLogado) {
        this.validarRole(usuarioLogado, Role.ADMIN);

        Usuario usuario = new Usuario();

        usuario.setNome(usuarioRequest.nome());
        usuario.setIdade(usuarioRequest.idade());
        usuario.setRole(usuarioRequest.role());
        usuario.setSenha(usuarioRequest.senha());

        return this.usuarioRepository.save(usuario);
    }

    public Usuario login(String nome, String senha) {
        Usuario usuario = usuarioRepository.findByNome(nome);
        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        if (!usuario.getSenha().equals(senha)) {
            throw new RuntimeException("Senha incorreta");
        }
        return usuario;
    }

    public List<Usuario> listar(){
        return this.usuarioRepository.findAll();
    }

    public Usuario buscarPorId(long id){
        return this.usuarioRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Usuario " + id + " Não encontrado")
                );
    }

    public Usuario atualizar(long id, UsuarioRequest usuarioRequest, Usuario usuarioLogado) {
        this.validarRole(usuarioLogado, Role.ADMIN);

        Usuario usuario1 = this.buscarPorId(id);

        usuario1.setNome(usuarioRequest.nome());
        usuario1.setSenha(usuarioRequest.senha());
        usuario1.setIdade(usuarioRequest.idade());
        usuario1.setRole(usuarioRequest.role());

        return this.usuarioRepository.save(usuario1);
    }

    public Usuario atualizarParcial(long id, UsuarioRequest usuarioRequest, Usuario usuarioLogado) {
        this.validarRole(usuarioLogado, Role.ADMIN);

        Usuario usuario = this.buscarPorId(id);

        if(usuarioRequest.nome() != null) usuario.setNome(usuarioRequest.nome());
        if(usuarioRequest.senha() != null) usuario.setSenha(usuarioRequest.senha());
        if(usuarioRequest.idade() != null) usuario.setIdade(usuarioRequest.idade());
        if(usuarioRequest.role() != null) usuario.setRole(usuarioRequest.role());

        return this.usuarioRepository.save(usuario);

    }

    public void deletarPorId(long id, Usuario usuarioLogado) {
        this.validarRole(usuarioLogado, Role.ADMIN);
        this.buscarPorId(id);
        this.usuarioRepository.deleteById(id);
    }



}
