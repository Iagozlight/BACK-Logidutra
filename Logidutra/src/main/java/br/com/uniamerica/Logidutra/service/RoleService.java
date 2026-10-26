package br.com.uniamerica.logidutra.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.uniamerica.logidutra.entity.Usuario;
import br.com.uniamerica.logidutra.enums.Role;
import br.com.uniamerica.logidutra.repository.UsuarioRepository;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class RoleService {

    private final UsuarioRepository usuarioRepository;

    public void validarRole(Long id, Role... rolesPermitidas) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o Id" + id));

        List<Role> listaDeRolesPermitidas = Arrays.asList(rolesPermitidas);

        if (!listaDeRolesPermitidas.contains(usuario.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado: Usuário não possui a permissão necessária.");
        }
    }
}
