package br.com.uniamerica.Logidutra.service;

import br.com.uniamerica.Logidutra.controller.dto.UsuarioRequest;
import br.com.uniamerica.Logidutra.entity.Usuario;
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

    public Usuario salvar(UsuarioRequest usuarioRequest) {
        Usuario usuario1 = new Usuario();

        usuario1.setNome(usuarioRequest.nome());
        usuario1.setIdade(usuarioRequest.idade());
        usuario1.setPerm(usuarioRequest.perm());
        usuario1.setSenha(usuarioRequest.senha());

        return this.usuarioRepository.save(usuario1);
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

    public Usuario atualizar(long id, UsuarioRequest usuarioRequest) {
        Usuario usuario1 = this.buscarPorId(id);

        usuario1.setNome(usuarioRequest.nome());
        usuario1.setSenha(usuarioRequest.senha());
        usuario1.setIdade(usuarioRequest.idade());
        usuario1.setPerm(usuarioRequest.perm());

        return this.usuarioRepository.save(usuario1);
    }

    public Usuario atualizarParcial(long id, UsuarioRequest usuarioRequest) {
        Usuario usuario = this.buscarPorId(id);

        if(usuarioRequest.nome() != null) usuario.setNome(usuarioRequest.nome());
        if(usuarioRequest.senha() != null) usuario.setSenha(usuarioRequest.senha());
        if(usuarioRequest.idade() != null) usuario.setIdade(usuarioRequest.idade());
        if(usuarioRequest.perm() != null) usuario.setPerm(usuarioRequest.perm());

        return this.usuarioRepository.save(usuario);

    }

    public void deletarPorId(long id) {
        this.buscarPorId(id);
        this.usuarioRepository.deleteById(id);
    }



}
