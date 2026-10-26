package br.com.uniamerica.Logidutra.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.server.ResponseStatusException;

import br.com.uniamerica.logidutra.controller.dto.UsuarioRequest;
import br.com.uniamerica.logidutra.entity.Usuario;
import br.com.uniamerica.logidutra.enums.Role;
import br.com.uniamerica.logidutra.enums.StatusOperacional;
import br.com.uniamerica.logidutra.repository.UsuarioRepository;
import br.com.uniamerica.logidutra.service.RoleService;
import br.com.uniamerica.logidutra.service.UsuarioService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    void salvar_deveLancarConflictQuandoNomeJaExiste() {
        when(usuarioRepository.existsByNomeIgnoreCase("Joao")).thenReturn(true);

        assertStatus(HttpStatus.CONFLICT, () -> usuarioService.salvar(
                new UsuarioRequest("Joao", "123", 20, Role.USER), admin));

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void salvar_deveLancarExcecaoQuandoUsuarioLogadoNaoEAdmin() {
        doThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "sem permissão"))
                .when(roleService).validarRole(2L, Role.ADMIN);

        assertStatus(HttpStatus.FORBIDDEN, () -> usuarioService.salvar(
                new UsuarioRequest("Maria", "123", 20, Role.USER), usuario));

        verify(usuarioRepository, never()).existsByNomeIgnoreCase(any());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void login_deveRetornarUsuarioQuandoCredenciaisCorretas() {
        when(usuarioRepository.findByNomeIgnoreCase("Joao")).thenReturn(usuario);

        Usuario resultado = usuarioService.login("  Joao ", "123");

        Assertions.assertEquals("Joao", resultado.getNome());
    }

    @Test
    void login_deveLancarUnauthorizedQuandoSenhaIncorreta() {
        when(usuarioRepository.findByNomeIgnoreCase("Joao")).thenReturn(usuario);

        assertStatus(HttpStatus.UNAUTHORIZED, () -> usuarioService.login("Joao", "999"));
    }

    @Test
    void login_deveLancarNotFoundQuandoUsuarioNaoExiste() {
        when(usuarioRepository.findByNomeIgnoreCase("Fantasma")).thenReturn(null);

        assertStatus(HttpStatus.NOT_FOUND, () -> usuarioService.login("Fantasma", "123"));
    }

    @Test
    void login_deveLancarNotFoundQuandoNomeNulo() {
        when(usuarioRepository.findByNomeIgnoreCase("")).thenReturn(null);

        assertStatus(HttpStatus.NOT_FOUND, () -> usuarioService.login(null, "123"));
    }

    @Test
    void redefinirSenha_deveAtualizarSenhaDoUsuario() {
        when(usuarioRepository.findByNomeIgnoreCase("Joao")).thenReturn(usuario);

        usuarioService.redefinirSenha(" Joao ", "nova123");

        Assertions.assertEquals("nova123", usuario.getSenha());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void redefinirSenha_deveLancarNotFoundQuandoUsuarioNaoExiste() {
        when(usuarioRepository.findByNomeIgnoreCase("Fantasma")).thenReturn(null);

        assertStatus(HttpStatus.NOT_FOUND, () -> usuarioService.redefinirSenha("Fantasma", "nova"));

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void redefinirSenha_deveLancarNotFoundQuandoNomeNulo() {
        when(usuarioRepository.findByNomeIgnoreCase("")).thenReturn(null);

        assertStatus(HttpStatus.NOT_FOUND, () -> usuarioService.redefinirSenha(null, "nova"));
    }

    @Test
    void redefinirSenha_deveLancarForbiddenQuandoUsuarioEAdmin() {
        when(usuarioRepository.findByNomeIgnoreCase("Admin")).thenReturn(admin);

        assertStatus(HttpStatus.FORBIDDEN, () -> usuarioService.redefinirSenha("Admin", "nova"));

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void listar_deveRetornarTodosOsUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario, admin));

        List<Usuario> resultado = usuarioService.listar();

        Assertions.assertEquals(2, resultado.size());
    }

    @Test
    void buscarPorId_deveRetornarUsuarioQuandoExiste() {
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.buscarPorId(2L);

        Assertions.assertEquals(2L, resultado.getId());
    }

    @Test
    void buscarPorId_deveLancarNotFoundQuandoNaoExiste() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertStatus(HttpStatus.NOT_FOUND, () -> usuarioService.buscarPorId(99L));
    }

    @Test
    void atualizar_deveSubstituirTodosOsCampos() {
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.atualizar(
                2L, new UsuarioRequest("Pedro", "senhaNova", 40, Role.ADMIN), admin);

        verify(roleService).validarRole(1L, Role.ADMIN);
        Assertions.assertEquals("Pedro", resultado.getNome());
        Assertions.assertEquals("senhaNova", resultado.getSenha());
        Assertions.assertEquals(40, resultado.getIdade());
        Assertions.assertEquals(Role.ADMIN, resultado.getRole());
    }

    @Test
    void atualizar_deveLancarExcecaoQuandoUsuarioLogadoNaoEAdmin() {
        doThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "sem permissão"))
                .when(roleService).validarRole(2L, Role.ADMIN);

        assertStatus(HttpStatus.FORBIDDEN, () -> usuarioService.atualizar(
                2L, new UsuarioRequest("Pedro", "x", 40, Role.USER), usuario));

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void atualizar_deveLancarNotFoundQuandoUsuarioNaoExiste() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertStatus(HttpStatus.NOT_FOUND, () -> usuarioService.atualizar(
                99L, new UsuarioRequest("Pedro", "x", 40, Role.USER), admin));
    }

    @Test
    void atualizarParcial_deveAtualizarSomenteCamposInformados() {
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.atualizarParcial(
                2L, new UsuarioRequest(null, null, null, null), admin);

        Assertions.assertEquals("Joao", resultado.getNome());
        Assertions.assertEquals("123", resultado.getSenha());
        Assertions.assertEquals(20, resultado.getIdade());
        Assertions.assertEquals(Role.USER, resultado.getRole());
    }

    @Test
    void atualizarParcial_deveAtualizarTodosOsCamposQuandoInformados() {
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.atualizarParcial(
                2L, new UsuarioRequest("Pedro", "nova", 50, Role.ADMIN), admin);

        verify(roleService).validarRole(1L, Role.ADMIN);
        Assertions.assertEquals("Pedro", resultado.getNome());
        Assertions.assertEquals("nova", resultado.getSenha());
        Assertions.assertEquals(50, resultado.getIdade());
        Assertions.assertEquals(Role.ADMIN, resultado.getRole());
    }

    @Test
    void marcarEmRota_deveAlterarStatusParaEmRota() {
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.marcarEmRota(2L);

        Assertions.assertEquals(StatusOperacional.EM_ROTA, resultado.getStatus());
    }

    @Test
    void marcarEmRota_deveLancarConflictQuandoJaEstaEmRota() {
        usuario.setStatus(StatusOperacional.EM_ROTA);
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuario));

        assertStatus(HttpStatus.CONFLICT, () -> usuarioService.marcarEmRota(2L));

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void marcarDisponivel_deveAlterarStatusParaDisponivel() {
        usuario.setStatus(StatusOperacional.EM_ROTA);
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.marcarDisponivel(2L);

        Assertions.assertEquals(StatusOperacional.DISPONIVEL, resultado.getStatus());
    }

    @Test
    void deletarPorId_deveRemoverUsuarioQuandoAdmin() {
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(usuario));

        usuarioService.deletarPorId(2L, admin);

        verify(roleService).validarRole(1L, Role.ADMIN);
        verify(usuarioRepository).deleteById(2L);
    }

    @Test
    void deletarPorId_deveLancarNotFoundQuandoUsuarioNaoExiste() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertStatus(HttpStatus.NOT_FOUND, () -> usuarioService.deletarPorId(99L, admin));

        verify(usuarioRepository, never()).deleteById(any());
    }

    @Test
    void deletarPorId_deveLancarExcecaoQuandoUsuarioLogadoNaoEAdmin() {
        doThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "sem permissão"))
                .when(roleService).validarRole(2L, Role.ADMIN);

        assertStatus(HttpStatus.FORBIDDEN, () -> usuarioService.deletarPorId(3L, usuario));

        verify(usuarioRepository, never()).deleteById(any());
    }

}
