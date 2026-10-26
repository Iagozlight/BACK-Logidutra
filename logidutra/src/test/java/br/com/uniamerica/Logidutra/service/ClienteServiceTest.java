package br.com.uniamerica.Logidutra.service;

import br.com.uniamerica.Logidutra.controller.dto.ClienteRequest;
import br.com.uniamerica.Logidutra.controller.dto.ViaCepResponse;
import br.com.uniamerica.Logidutra.data.feign.ViaCepClient;
import br.com.uniamerica.Logidutra.entity.ClienteEntity;
import br.com.uniamerica.Logidutra.entity.Usuario;
import br.com.uniamerica.Logidutra.enums.Role;
import br.com.uniamerica.Logidutra.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private ViaCepClient viaCepClient;
    @Mock
    private RoleService roleService;

    @InjectMocks
    private ClienteService clienteService;

    private Usuario usuario;

    @BeforeEach
    void prepararUsuario() {
        usuario = new Usuario();
        usuario.setId(1L);
    }

    @Test
    void salvaClienteComEnderecoRetornadoPeloCep() {
        ClienteRequest request = request("Gabriel", "85850-000");
        when(viaCepClient.buscarPorCep("85850-000")).thenReturn(endereco("85850-000", "Rua das Flores"));
        when(clienteRepository.save(any(ClienteEntity.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        ClienteEntity cliente = clienteService.salvar(request, usuario);

        assertEquals("Gabriel", cliente.getNome());
        assertEquals("Rua das Flores", cliente.getLogradouro());
        assertEquals("Centro", cliente.getBairro());
        assertEquals("Foz do Iguaçu", cliente.getCidade());
        verify(roleService).validarRole(1L, Role.ADMIN);
        verify(clienteRepository).save(any(ClienteEntity.class));
    }

    @Test
    void naoSalvaQuandoCepNaoExiste() {
        when(viaCepClient.buscarPorCep("00000-000"))
                .thenReturn(new ViaCepResponse(null, null, null, null, true));

        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> clienteService.salvar(request("Gabriel", "00000-000"), usuario));

        assertEquals(HttpStatus.BAD_REQUEST, erro.getStatusCode());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void listaClientes() {
        when(clienteRepository.findAll()).thenReturn(List.of(cliente()));

        List<ClienteEntity> clientes = clienteService.listar();

        assertEquals(1, clientes.size());
        assertEquals("Gabriel", clientes.get(0).getNome());
    }

    @Test
    void buscaClientePorId() {
        ClienteEntity esperado = cliente();
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(esperado));

        assertSame(esperado, clienteService.buscarPorId(1L));
    }

    @Test
    void retornaNaoEncontradoQuandoBuscaIdInexistente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> clienteService.buscarPorId(1L));

        assertEquals(HttpStatus.NOT_FOUND, erro.getStatusCode());
    }

    @Test
    void listaVaziaQuandoNaoHaClientes() {
        when(clienteRepository.findAll()).thenReturn(List.of());

        assertTrue(clienteService.listar().isEmpty());
    }

    @Test
    void naoAtualizaClienteQueNaoExiste() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> clienteService.atualizar(99L, request("Maria", "85850-000"), usuario));

        assertEquals(HttpStatus.NOT_FOUND, erro.getStatusCode());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void naoDeletaClienteInexistente() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> clienteService.deletar(99L, usuario));
        verify(clienteRepository, never()).delete(any(ClienteEntity.class));
    }

    @Test
    void rejeitaCepNulo() {
        when(viaCepClient.buscarPorCep("85850-000")).thenReturn(null);

        ResponseStatusException erro = assertThrows(ResponseStatusException.class,
                () -> clienteService.salvar(request("Gabriel", "85850-000"), usuario));

        assertEquals(HttpStatus.BAD_REQUEST, erro.getStatusCode());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void atualizaDadosEEnderecoDoCliente() {
        ClienteEntity existente = cliente();
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(viaCepClient.buscarPorCep("85876-113")).thenReturn(endereco("85876-113", "Rua Nova"));
        when(clienteRepository.save(any(ClienteEntity.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        ClienteEntity atualizado = clienteService.atualizar(1L, request("Maria", "85876-113"), usuario);

        assertEquals("Maria", atualizado.getNome());
        assertEquals("Rua Nova", atualizado.getLogradouro());
        assertEquals("85876-113", atualizado.getCep());
        verify(clienteRepository).save(existente);
    }

    @Test
    void atualizaParcialSemCep() {
        ClienteEntity existente = cliente();
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(clienteRepository.save(existente)).thenReturn(existente);
        ClienteRequest alteracao = new ClienteRequest("Maria", null, null, "85850-000");

        ClienteEntity atualizado = clienteService.atualizarParcial(1L, alteracao, usuario);

        assertEquals("Maria", atualizado.getNome());
        assertEquals("123.456.789-00", atualizado.getCpf());
        assertEquals("Rua das Flores", atualizado.getLogradouro());
        verifyNoInteractions(viaCepClient);
    }

    @Test
    void atualizaCepNaAlteracaoParcial() {
        ClienteEntity existente = cliente();
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(viaCepClient.buscarPorCep("85876-113")).thenReturn(endereco("85876-113", "Rua Nova"));
        when(clienteRepository.save(existente)).thenReturn(existente);
        ClienteRequest alteracao = new ClienteRequest(null, null, null, "85876-113");

        ClienteEntity atualizado = clienteService.atualizarParcial(1L, alteracao, usuario);

        assertEquals("85876-113", atualizado.getCep());
        assertEquals("Rua Nova", atualizado.getLogradouro());
    }

    @Test
    void alteracaoParcialMantemCepIgualSemConsultarViaCep() {
        ClienteEntity existente = cliente();
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(clienteRepository.save(existente)).thenReturn(existente);

        ClienteEntity atualizado = clienteService.atualizarParcial(1L,
                new ClienteRequest(null, null, "45888888888", "85850-000"), usuario);

        assertEquals("45888888888", atualizado.getTelefone());
        verifyNoInteractions(viaCepClient);
    }

    @Test
    void deletaClienteExistente() {
        ClienteEntity existente = cliente();
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(existente));

        clienteService.deletar(1L, usuario);

        verify(roleService).validarRole(1L, Role.ADMIN);
        verify(clienteRepository).delete(existente);
    }

    private ClienteRequest request(String nome, String cep) {
        return new ClienteRequest(nome, "123.456.789-00", "45999999999", cep);
    }

    private ViaCepResponse endereco(String cep, String logradouro) {
        return new ViaCepResponse(cep, logradouro, "Centro", "Foz do Iguaçu", false);
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
        cliente.setCidade("Foz do Iguaçu");
        return cliente;
    }
}
