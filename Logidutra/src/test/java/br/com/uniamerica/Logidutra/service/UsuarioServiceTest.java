package br.com.uniamerica.Logidutra.service;

import br.com.uniamerica.Logidutra.controller.dto.UsuarioRequest;
import br.com.uniamerica.Logidutra.entity.Usuario;
import br.com.uniamerica.Logidutra.enums.Role;
import br.com.uniamerica.Logidutra.enums.StatusOperacional;
import br.com.uniamerica.Logidutra.repository.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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

    private Usuario usuario;
    private Usuario admin;

    @BeforeEach
    void setup() {
        usuario = new Usuario();
        usuario.setId(2L);
        usuario.setNome("Joao");
        usuario.setSenha("123");
        usuario.setIdade(20);
        usuario.setRole(Role.USER);
        usuario.setStatus(StatusOperacional.DISPONIVEL);

        admin = new Usuario();
        admin.setId(1L);
        admin.setNome("Admin");
        admin.setRole(Role.ADMIN);

        when(usuarioRepository.save(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    private ResponseStatusException assertStatus(HttpStatus esperado, org.junit.jupiter.api.function.Executable acao) {
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, acao);
        Assertions.assertEquals(esperado, exception.getStatusCode());
        return exception;
    }

    @Test
    void salvar_deveCriarUsuarioComNomeSemEspacosEStatusDisponivel() {
        when(usuarioRepository.existsByNomeIgnoreCase("Maria")).thenReturn(false);

        Usuario resultado = usuarioService.salvar(
                new UsuarioRequest("  Maria  ", "abc", 25, Role.USER), admin);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        verify(roleService).validarRole(1L, Role.ADMIN);

        Usuario salvo = captor.getValue();
        Assertions.assertEquals("Maria", salvo.getNome());
        Assertions.assertEquals("abc", salvo.getSenha());
        Assertions.assertEquals(25, salvo.getIdade());
        Assertions.assertEquals(Role.USER, salvo.getRole());
        Assertions.assertEquals(StatusOperacional.DISPONIVEL, salvo.getStatus());
        Assertions.assertEquals("Maria", resultado.getNome());
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

    @Test
    void cenario04() {

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setStatus(StatusOperacional.DISPONIVEL);

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        when(usuarioRepository.save(any()))
                .thenReturn(usuario);

        Usuario resultado =
                usuarioService.marcarEmRota(1L);

        Assertions.assertEquals(
                StatusOperacional.EM_ROTA,
                resultado.getStatus()
        );
    }

    @Test
    void cenario05() {

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setStatus(StatusOperacional.EM_ROTA);

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        when(usuarioRepository.save(any()))
                .thenReturn(usuario);

        Usuario resultado =
                usuarioService.marcarDisponivel(1L);

        Assertions.assertEquals(
                StatusOperacional.DISPONIVEL,
                resultado.getStatus()
        );
    }

    @Test
    void cenario06() {

        when(usuarioRepository.findById(99L))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                ResponseStatusException.class,
                () -> usuarioService.buscarPorId(99L)
        );
    }


}
