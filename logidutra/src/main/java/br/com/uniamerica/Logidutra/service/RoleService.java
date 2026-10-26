package br.com.uniamerica.Logidutra.service;

import br.com.uniamerica.Logidutra.entity.Usuario;
import br.com.uniamerica.Logidutra.enums.Role;
import br.com.uniamerica.Logidutra.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            throw new RuntimeException("Acesso negado: Usuário não possui a permissão necessária.");
        }
    }
}
