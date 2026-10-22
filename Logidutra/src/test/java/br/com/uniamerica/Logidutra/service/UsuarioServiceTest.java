package br.com.uniamerica.Logidutra.service;

import br.com.uniamerica.Logidutra.controller.dto.UsuarioRequest;
import br.com.uniamerica.Logidutra.entity.Usuario;
import br.com.uniamerica.Logidutra.enums.Role;
import br.com.uniamerica.Logidutra.repository.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UsuarioServiceTest {

    @Autowired
    UsuarioService usuarioService;

    @MockitoBean
    UsuarioRepository usuarioRepository;

    @MockitoBean
    RoleService roleService;

    @BeforeEach
    void setup() {

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Joao");
        usuario.setSenha("123");
        usuario.setRole(Role.USER);

        when(usuarioRepository.findByNomeIgnoreCase("Joao"))
                .thenReturn(usuario);
    }

    @Test
    void cenario01() {

        Assertions.assertThrows(
                ResponseStatusException.class,
                () -> usuarioService.login("Joao", "999")
        );
    }

    @Test
    void cenario02() {

        Usuario admin = new Usuario();
        admin.setId(1L);

        when(usuarioRepository.existsByNomeIgnoreCase(any()))
                .thenReturn(false);

        when(usuarioRepository.save(any()))
                .thenReturn(new Usuario());

        usuarioService.salvar(
                new UsuarioRequest("Joao", "123", 20, Role.USER),
                admin
        );

        verify(roleService)
                .validarRole(1L, Role.ADMIN);
    }

    @Test
    void cenario03() {

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.buscarPorId(1L);

        Assertions.assertEquals(1L, resultado.getId());
    }

}
