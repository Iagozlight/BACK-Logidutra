package br.com.uniamerica.Logidutra.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.uniamerica.logidutra.controller.ClienteController;
import br.com.uniamerica.logidutra.controller.dto.ClienteRequest;
import br.com.uniamerica.logidutra.entity.ClienteEntity;
import br.com.uniamerica.logidutra.entity.Usuario;
import br.com.uniamerica.logidutra.service.ClienteService;
import br.com.uniamerica.logidutra.service.UsuarioService;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private ClienteController clienteController;

    @Test
    void salvarComSucesso() {
        when(usuarioService.buscarPorId(1L)).thenReturn(criarUsuario());
        when(clienteService.salvar(any(ClienteRequest.class), any(Usuario.class))).thenReturn(criarClienteEntity());

        ResponseEntity<?> resposta = clienteController.salvar(criarClienteRequest(), 1L);

        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
    }

    @Test
    void retornaBadRequestQuandoFalhaAoSalvar() {
        when(usuarioService.buscarPorId(1L)).thenThrow(new RuntimeException());

        ResponseEntity<?> resposta = clienteController.salvar(criarClienteRequest(), 1L);

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
    }

    @Test
    void listaClientesComSucesso() {
        when(clienteService.listar()).thenReturn(List.of(criarClienteEntity()));

        ResponseEntity<?> resposta = clienteController.listar();

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(1, ((List<?>) resposta.getBody()).size());
    }

    @Test
    void retornaBadRequestQuandoFalhaAoListar() {
        when(clienteService.listar()).thenThrow(new RuntimeException());
        ResponseEntity<?> resposta = clienteController.listar();

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
    }

    @Test
    void buscaClientePorIdComSucesso() {
        when(clienteService.buscarPorId(1L)).thenReturn(criarClienteEntity());

        ResponseEntity<?> resposta = clienteController.buscarPorId(1L);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

    @Test
    void retornaNotFoundQuandoClienteNaoExisteNaBuscaPorId() {
        when(clienteService.buscarPorId(1L)).thenThrow(new NoSuchElementException());

        ResponseEntity<?> resposta = clienteController.buscarPorId(1L);

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void retornaBadRequestQuandoFalhaNaBuscaPorId() {
        when(clienteService.buscarPorId(1L)).thenThrow(new RuntimeException());

        ResponseEntity<?> resposta = clienteController.buscarPorId(1L);

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
    }

    @Test
    void atualizarComSucesso() {
        when(usuarioService.buscarPorId(1L)).thenReturn(criarUsuario());
        when(clienteService.atualizar(eq(1L), any(ClienteRequest.class), any(Usuario.class))).thenReturn(criarClienteEntity());

        ResponseEntity<?> resposta = clienteController.atualizar(1L, criarClienteRequest(), 1L);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

    @Test
    void retornaNoContentQuandoClienteNaoExisteAoAtualizar() {
        when(usuarioService.buscarPorId(1L)).thenReturn(criarUsuario());
        when(clienteService.atualizar(eq(1L), any(ClienteRequest.class), any(Usuario.class)))
                .thenThrow(new NoSuchElementException());

        ResponseEntity<?> resposta = clienteController.atualizar(1L, criarClienteRequest(), 1L);

        assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());
    }

    @Test
    void retornaBadRequestQuandoFalhaAoAtualizar() {
        when(usuarioService.buscarPorId(1L)).thenThrow(new RuntimeException());

        ResponseEntity<?> resposta = clienteController.atualizar(1L, criarClienteRequest(), 1L);

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
    }

    @Test
    void atualizarParcialComSucesso() {
        when(usuarioService.buscarPorId(1L)).thenReturn(criarUsuario());
        when(clienteService.atualizarParcial(eq(1L), any(ClienteRequest.class), any(Usuario.class)))
                .thenReturn(criarClienteEntity());

        ResponseEntity<?> resposta = clienteController.atualizarParcial(1L, criarClienteRequest(), 1L);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

    @Test
    void retornaNotFoundQuandoClienteNaoExisteAoAtualizarParcial() {
        when(usuarioService.buscarPorId(1L)).thenReturn(criarUsuario());
        when(clienteService.atualizarParcial(eq(1L), any(ClienteRequest.class), any(Usuario.class)))
                .thenThrow(new NoSuchElementException());

        ResponseEntity<?> resposta = clienteController.atualizarParcial(1L, criarClienteRequest(), 1L);

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void retornaBadRequestQuandoFalhaAoAtualizarParcial() {
        when(usuarioService.buscarPorId(1L)).thenThrow(new RuntimeException());

        ResponseEntity<?> resposta = clienteController.atualizarParcial(1L, criarClienteRequest(), 1L);

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
    }

    @Test
    void deletarComSucesso() {
        when(usuarioService.buscarPorId(1L)).thenReturn(criarUsuario());

        ResponseEntity<?> resposta = clienteController.deletar(1L, 1L);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        verify(clienteService).deletar(eq(1L), any(Usuario.class));
    }

    @Test
    void retornaNotFoundQuandoClienteNaoExisteAoDeletar() {
        when(usuarioService.buscarPorId(1L)).thenReturn(criarUsuario());
        doThrow(new NoSuchElementException()).when(clienteService).deletar(eq(1L), any(Usuario.class));

        ResponseEntity<?> resposta = clienteController.deletar(1L, 1L);

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void retornaBadRequestQuandoFalhaAoDeletar() {
        when(usuarioService.buscarPorId(1L)).thenThrow(new RuntimeException());

        ResponseEntity<?> resposta = clienteController.deletar(1L, 1L);

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
    }

    private ClienteEntity criarClienteEntity() {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setId(1L);
        cliente.setNome("Gabriel");
        cliente.setCpf("123.456.789-00");
        cliente.setTelefone("45999999999");
        cliente.setCep("85850-000");
        cliente.setLogradouro("Rua das Flores");
        cliente.setBairro("Centro");
        cliente.setCidade("Foz do Iguacu");
        return cliente;
    }

    private ClienteRequest criarClienteRequest() {
        return new ClienteRequest("Gabriel", "123.456.789-00", "45999999999", "85850-000");
    }

    private Usuario criarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Administrador");
        return usuario;
    }
}
