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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {
    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ViaCepClient viaCepClient;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private ClienteService clienteService;

    private Usuario usuarioAdmin;
    private ClienteRequest clienteRequest;
    private ViaCepResponse viaCepResponse;

    @BeforeEach
    void setUp() {
        //Ele é apenas um modelo base
        usuarioAdmin = new Usuario();
        usuarioAdmin.setId(1L);

        clienteRequest = new ClienteRequest("Gabriel", "123.456.789-00", "45999999999", "85850-000");
        viaCepResponse = new ViaCepResponse("85850-000", "Rua das Flores", "Centro", "Foz do Iguaçu", false);
    }

    @Test
    @DisplayName("A entidade cliente precisa ser salva corretamente e com sucesso.")
    void salvar_clienteCorretamente() {
        when(viaCepClient.buscarPorCep(clienteRequest.cep())).thenReturn(viaCepResponse);
        when(clienteRepository.save(any(ClienteEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ClienteEntity resultado = clienteService.salvar(clienteRequest, usuarioAdmin);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("Gabriel", resultado.getNome());
        Assertions.assertEquals("Rua das Flores", resultado.getLogradouro());
        Assertions.assertEquals("123.456.789-00", resultado.getCpf());
        Assertions.assertEquals("45999999999", resultado.getTelefone());
        Assertions.assertEquals("85850-000", resultado.getCep());

        //O Verify ele validda se tá tudo certo 
        verify(roleService).validarRole(1L, Role.ADMIN);
        verify(clienteRepository).save(any(ClienteEntity.class));
        verify(viaCepClient).buscarPorCep(clienteRequest.cep());
        verifyNoMoreInteractions(viaCepClient, roleService);
    }


}
