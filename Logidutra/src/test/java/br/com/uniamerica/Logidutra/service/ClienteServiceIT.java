package br.com.uniamerica.Logidutra.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import br.com.uniamerica.logidutra.controller.dto.ClienteRequest;
import br.com.uniamerica.logidutra.controller.dto.ViaCepResponse;
import br.com.uniamerica.logidutra.data.feign.ViaCepClient;
import br.com.uniamerica.logidutra.entity.ClienteEntity;
import br.com.uniamerica.logidutra.entity.Usuario;
import br.com.uniamerica.logidutra.enums.Role;
import br.com.uniamerica.logidutra.repository.ClienteRepository;
import br.com.uniamerica.logidutra.service.ClienteService;
import br.com.uniamerica.logidutra.service.RoleService;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ClienteServiceIT {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteRepository clienteRepository;

    @MockitoBean
    private ViaCepClient viaCepClient;

    @MockitoBean
    private RoleService roleService;

    private Usuario usuario;

    @BeforeEach
    void prepararUsuario() {
        usuario = new Usuario();
        usuario.setId(1L);
        doNothing().when(roleService).validarRole(1L, Role.ADMIN);
    }

    @Test
    void salvaClienteEConsultaNoBanco() {
        String sufixo = UUID.randomUUID().toString().substring(0, 8);
        String nome = "Cliente-" + sufixo;
        String cpf = "123." + sufixo.substring(0, 3) + ".456-00";

        when(viaCepClient.buscarPorCep("85850-000")).thenReturn(new ViaCepResponse("85850-000", "Rua das Flores", "Centro", "Foz do Iguaçu", false));

        ClienteRequest request = new ClienteRequest(nome, cpf, "45999999999", "85850-000");
        ClienteEntity salvo = clienteService.salvar(request, usuario);

        assertNotNull(salvo.getId());
        assertEquals(nome, salvo.getNome());
        assertEquals("Rua das Flores", salvo.getLogradouro());
        assertEquals("Centro", salvo.getBairro());
        assertEquals("Foz do Iguaçu", salvo.getCidade());

        ClienteEntity encontrado = clienteRepository.findById(salvo.getId()).orElseThrow();

        assertEquals(salvo.getId(), encontrado.getId());
        assertEquals(nome, encontrado.getNome());
        assertEquals("85850-000", encontrado.getCep());
    }

    @Test
    void listaClientesPersistidosNoBanco() {
        String sufixo = UUID.randomUUID().toString().substring(0, 8);
        String nome = "Cliente-" + sufixo;
        String cpf = "321." + sufixo.substring(0, 3) + ".654-00";
        when(viaCepClient.buscarPorCep("85850-000")).thenReturn(new ViaCepResponse("85850-000", "Rua das Flores", "Centro", "Foz do Iguaçu", false));

        clienteService.salvar(new ClienteRequest(nome, cpf, "45999999999", "85850-000"), usuario);
        List<ClienteEntity> clientes = clienteService.listar();

        assertTrue(clientes.stream().anyMatch(cliente -> cliente.getNome().equals(nome)));
    }
}