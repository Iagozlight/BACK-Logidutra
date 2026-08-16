package br.com.uniamerica.Logidutra.service;

import br.com.uniamerica.Logidutra.controller.dto.UsuarioRequest;
import br.com.uniamerica.Logidutra.entity.Usuario;
import br.com.uniamerica.Logidutra.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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


}
