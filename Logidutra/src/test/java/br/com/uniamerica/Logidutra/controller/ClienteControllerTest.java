package br.com.uniamerica.Logidutra.controller;

import br.com.uniamerica.Logidutra.entity.ClienteEntity;
import br.com.uniamerica.Logidutra.service.ClienteService;
import br.com.uniamerica.Logidutra.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private ClienteController clienteController;

    @Test
    void listaClientesComSucesso() {
        ClienteEntity cliente = cliente();
        when(clienteService.listar()).thenReturn(List.of(cliente));

        ResponseEntity<?> resposta = clienteController.listar();

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(1, ((List<?>) resposta.getBody()).size());
    }

    @Test
    void buscaClientePorIdComSucesso() {
        when(clienteService.buscarPorId(1L)).thenReturn(cliente());

        ResponseEntity<?> resposta = clienteController.buscarPorId(1L);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

    @Test
    void retornaBadRequestQuandoFalhaAoListar() {
        when(clienteService.listar()).thenThrow(new RuntimeException());

        ResponseEntity<?> resposta = clienteController.listar();

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
    }

    private ClienteEntity cliente() {
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
}