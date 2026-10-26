package br.com.uniamerica.Logidutra.controller;

import br.com.uniamerica.Logidutra.controller.dto.UsuarioRequest;
import br.com.uniamerica.Logidutra.controller.dto.UsuarioResponse;
import br.com.uniamerica.Logidutra.entity.Usuario;
import br.com.uniamerica.Logidutra.enums.Role;
import br.com.uniamerica.Logidutra.enums.StatusOperacional;
import br.com.uniamerica.Logidutra.service.UsuarioService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UsuarioControllerTest {

    @Autowired
    UsuarioController usuarioController;

    @MockitoBean
    UsuarioService usuarioService;

    private Usuario usuario;
    private Usuario admin;
    private UsuarioRequest request;

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
        admin.setSenha("admin");
        admin.setIdade(30);
        admin.setRole(Role.ADMIN);
        admin.setStatus(StatusOperacional.DISPONIVEL);

        request = new UsuarioRequest("Joao", "123", 20, Role.USER);
    }


    @Test
    void salvar_deveRetornarCreated() {
        when(usuarioService.buscarPorId(1L)).thenReturn(admin);
        when(usuarioService.salvar(any(), any())).thenReturn(usuario);

        ResponseEntity<?> response = usuarioController.salvar(request, 1L);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    void salvar_deveRetornarStatusEMensagemQuandoResponseStatusException() {
        when(usuarioService.buscarPorId(1L)).thenReturn(admin);
        when(usuarioService.salvar(any(), any()))
                .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um usuário"));

        ResponseEntity<?> response = usuarioController.salvar(request, 1L);

        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assertions.assertEquals("Já existe um usuário", response.getBody());
    }

    @Test
    void salvar_deveRetornarBadRequestQuandoExcecaoGenerica() {
        when(usuarioService.buscarPorId(1L)).thenReturn(admin);
        when(usuarioService.salvar(any(), any())).thenThrow(new RuntimeException("erro inesperado"));

        ResponseEntity<?> response = usuarioController.salvar(request, 1L);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("erro inesperado", response.getBody());
    }


    @Test
    void login_deveRetornarOk() {
        when(usuarioService.login("Joao", "123")).thenReturn(usuario);

        ResponseEntity<UsuarioResponse> response = usuarioController.login("Joao", "123");

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Joao", response.getBody().nome());
    }

    @Test
    void login_deveRetornarStatusDaExcecao() {
        when(usuarioService.login("Joao", "errada"))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha incorreta"));

        ResponseEntity<UsuarioResponse> response = usuarioController.login("Joao", "errada");

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }


    @Test
    void listar_deveRetornarListaDeUsuarios() {
        when(usuarioService.listar()).thenReturn(List.of(usuario, admin));

        ResponseEntity<List<UsuarioResponse>> response = usuarioController.listar();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(2, response.getBody().size());
    }

    @Test
    void listar_deveRetornarBadRequestQuandoOcorreErro() {
        when(usuarioService.listar()).thenThrow(new RuntimeException("falha"));

        ResponseEntity<List<UsuarioResponse>> response = usuarioController.listar();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    void buscarPorId_deveRetornarOk() {
        when(usuarioService.buscarPorId(2L)).thenReturn(usuario);

        ResponseEntity<UsuarioResponse> response = usuarioController.buscarPorId(2L);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Joao", response.getBody().nome());
    }

    @Test
    void buscarPorId_deveRetornarNotFoundQuandoResponseStatusException() {
        when(usuarioService.buscarPorId(99L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "não encontrado"));

        ResponseEntity<UsuarioResponse> response = usuarioController.buscarPorId(99L);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void buscarPorId_deveRetornarBadRequestQuandoExcecaoGenerica() {
        when(usuarioService.buscarPorId(99L)).thenThrow(new RuntimeException("falha"));

        ResponseEntity<UsuarioResponse> response = usuarioController.buscarPorId(99L);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    void atualizar_deveRetornarOk() {
        when(usuarioService.buscarPorId(1L)).thenReturn(admin);
        when(usuarioService.atualizar(eq(2L), any(), any())).thenReturn(usuario);

        ResponseEntity<UsuarioResponse> response = usuarioController.atualizar(2L, request, 1L);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Joao", response.getBody().nome());
    }

    @Test
    void atualizar_deveRetornarBadRequestQuandoServiceFalha() {
        when(usuarioService.buscarPorId(1L)).thenReturn(admin);
        when(usuarioService.atualizar(eq(2L), any(), any()))
                .thenThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "sem permissão"));

        ResponseEntity<UsuarioResponse> response = usuarioController.atualizar(2L, request, 1L);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    void atualizarParcial_deveRetornarOk() {
        when(usuarioService.buscarPorId(1L)).thenReturn(admin);
        when(usuarioService.atualizarParcial(eq(2L), any(), any())).thenReturn(usuario);

        ResponseEntity<UsuarioResponse> response = usuarioController.atualizarParcial(2L, request, 1L);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void atualizarParcial_deveRetornarBadRequestQuandoUsuarioLogadoNaoExiste() {
        when(usuarioService.buscarPorId(1L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "não encontrado"));

        ResponseEntity<UsuarioResponse> response = usuarioController.atualizarParcial(2L, request, 1L);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    void redefinirSenha_deveRetornarOk() {
        ResponseEntity<?> response = usuarioController.redefinirSenha("Joao", "nova123");

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void redefinirSenha_deveRetornarStatusEMensagemQuandoResponseStatusException() {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"))
                .when(usuarioService).redefinirSenha("Joao", "nova123");

        ResponseEntity<?> response = usuarioController.redefinirSenha("Joao", "nova123");

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("Usuário não encontrado", response.getBody());
    }

    @Test
    void redefinirSenha_deveRetornarBadRequestQuandoExcecaoGenerica() {
        doThrow(new RuntimeException("falha"))
                .when(usuarioService).redefinirSenha("Joao", "nova123");

        ResponseEntity<?> response = usuarioController.redefinirSenha("Joao", "nova123");

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Erro ao redefinir senha.", response.getBody());
    }


    @Test
    void marcarEmRota_deveRetornarOk() {
        usuario.setStatus(StatusOperacional.EM_ROTA);
        when(usuarioService.marcarEmRota(2L)).thenReturn(usuario);

        ResponseEntity<UsuarioResponse> response = usuarioController.marcarEmRota(2L);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void marcarEmRota_deveRetornarBadRequestQuandoJaEstaEmRota() {
        when(usuarioService.marcarEmRota(2L))
                .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Motorista já está em rota"));

        ResponseEntity<UsuarioResponse> response = usuarioController.marcarEmRota(2L);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    void marcarDisponivel_deveRetornarOk() {
        when(usuarioService.marcarDisponivel(2L)).thenReturn(usuario);

        ResponseEntity<UsuarioResponse> response = usuarioController.marcarDisponivel(2L);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void marcarDisponivel_deveRetornarBadRequestQuandoOcorreErro() {
        when(usuarioService.marcarDisponivel(2L)).thenThrow(new RuntimeException("falha"));

        ResponseEntity<UsuarioResponse> response = usuarioController.marcarDisponivel(2L);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    void deletar_deveRetornarNoContent() {
        when(usuarioService.buscarPorId(1L)).thenReturn(admin);

        ResponseEntity<Void> response = usuarioController.deletar(2L, 1L);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deletar_deveRetornarBadRequestQuandoServiceFalha() {
        when(usuarioService.buscarPorId(1L)).thenReturn(admin);
        doThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "sem permissão"))
                .when(usuarioService).deletarPorId(eq(2L), any());

        ResponseEntity<Void> response = usuarioController.deletar(2L, 1L);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}