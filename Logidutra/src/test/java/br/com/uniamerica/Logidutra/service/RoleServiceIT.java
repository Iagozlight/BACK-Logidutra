package br.com.uniamerica.Logidutra.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import br.com.uniamerica.logidutra.entity.Usuario;
import br.com.uniamerica.logidutra.enums.Role;
import br.com.uniamerica.logidutra.repository.UsuarioRepository;
import br.com.uniamerica.logidutra.service.RoleService;

import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;



@SpringBootTest
@ActiveProfiles("test")
@Transactional
class RoleServiceIT {
    @Autowired
    private RoleService roleService;

    @Autowired
    private UsuarioRepository usuarioRepository;



    @Test
    void validarRoleAdminNoBanco() {
        Usuario usuario = usuarioRepository.saveAndFlush(usuario(Role.ADMIN));

        assertDoesNotThrow(() -> roleService.validarRole(usuario.getId(), Role.ADMIN));
    }

    @Test
    void validarRoleNegadaNoBanco() {
        Usuario usuario = usuarioRepository.saveAndFlush(usuario(Role.USER));

        assertThrows(RuntimeException.class, () -> roleService.validarRole(usuario.getId(), Role.ADMIN));

    }

    @Test
    void validarRoleInexistenteNoBanco() {
        assertThrows(RuntimeException.class, () -> roleService.validarRole(Long.MAX_VALUE, Role.ADMIN));

    }

    private Usuario usuario(Role role) {
        Usuario usuario = new Usuario();
        usuario.setNome("RoleTest-" + UUID.randomUUID());
        usuario.setSenha("senha");
        usuario.setRole(role);

        return usuario;
    }

}