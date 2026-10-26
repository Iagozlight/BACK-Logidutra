package br.com.uniamerica.logidutra.service;

import br.com.uniamerica.logidutra.entity.Usuario;
import br.com.uniamerica.logidutra.enums.Role;
import br.com.uniamerica.logidutra.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    void validarRoleAdminFuncional() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario(Role.ADMIN)));

        assertDoesNotThrow(() -> roleService.validarRole(1L, Role.ADMIN));
        verify(usuarioRepository).findById(1L);
    }

    @Test
    void validarRoleUsuarioFuncional() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario(Role.USER)));

        assertDoesNotThrow(() -> roleService.validarRole(1L, Role.ADMIN, Role.USER));
    }

    @Test
    void validarRoleSemPermissao() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario(Role.USER)));

        assertThrows(RuntimeException.class, () -> roleService.validarRole(1L, Role.ADMIN));
    }

    @Test
    void validarRoleSemRolesPermitidas() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario(Role.ADMIN)));

        assertThrows(RuntimeException.class, () -> roleService.validarRole(1L));
    }

    @Test
    void validarRoleUsuarioInexistente() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> roleService.validarRole(99L, Role.ADMIN));
    }

    private Usuario usuario(Role role) {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setRole(role);
        return usuario;
    }
}
