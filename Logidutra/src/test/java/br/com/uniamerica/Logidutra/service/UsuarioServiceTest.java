package br.com.uniamerica.Logidutra.service;

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
}
