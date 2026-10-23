package br.com.uniamerica.Logidutra.service;

import br.com.uniamerica.Logidutra.controller.dto.ClienteRequest;
import br.com.uniamerica.Logidutra.controller.dto.ClienteResponse;
import br.com.uniamerica.Logidutra.controller.dto.ViaCepResponse;
import br.com.uniamerica.Logidutra.data.feign.ViaCepClient;
import br.com.uniamerica.Logidutra.entity.ClienteEntity;
import br.com.uniamerica.Logidutra.entity.Usuario;
import br.com.uniamerica.Logidutra.enums.Role;
import br.com.uniamerica.Logidutra.enums.StatusOperacional;
import br.com.uniamerica.Logidutra.repository.ClienteRepository;
import feign.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {
    @Mock
    ClienteRepository clienteRepository;
    @Mock
    ViaCepClient viaCepClient;
    @Mock
    RoleService roleService;

    @InjectMocks
    ClienteService clienteService;

    private Usuario usuario;
    private ViaCepResponse cepValido;
    private ViaCepResponse cepInvalido;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);

        cepValido = new ViaCepResponse("85850-000", "Rua das Flores", "Centro", "Foz do Iguaçu", false);
        cepInvalido = new ViaCepResponse(null, null, null, null, true);
    }


    private ClienteEntity clienteExistente() {
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

    @Test
    void salvarClienteValidoCorretamente() {
        ClienteRequest request = new ClienteRequest("Gabriel", "123.456.789-00", "45999999999", "85850-000");
        when(viaCepClient.buscarPorCep("85850-000")).thenReturn(cepValido);
        when(clienteRepository.save(any(ClienteEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        var cliente = clienteService.salvar(request, usuario);


        assertEquals("Gabriel", cliente.getNome());
        assertEquals("123.456.789-00", cliente.getCpf());
        assertEquals("45999999999", cliente.getTelefone());
        assertEquals("85850000", cliente.getCep());
        assertEquals("Rua das Flores", cliente.getLogradouro());
        assertEquals("Centro", cliente.getBairro());
        assertEquals("Foz do Iguaçu", cliente.getCidade());

        verify(roleService).validarRole(1L, Role.ADMIN);
        verify(clienteRepository).save(any(ClienteEntity.class));
    }



}
