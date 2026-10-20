package br.com.uniamerica.Logidutra.service;

import br.com.uniamerica.Logidutra.controller.dto.ClienteRequest;
import br.com.uniamerica.Logidutra.controller.dto.ClienteResponse;
import br.com.uniamerica.Logidutra.data.feign.ViaCepClient;
import br.com.uniamerica.Logidutra.entity.ClienteEntity;
import br.com.uniamerica.Logidutra.entity.Usuario;
import br.com.uniamerica.Logidutra.enums.Role;
import br.com.uniamerica.Logidutra.enums.StatusOperacional;
import br.com.uniamerica.Logidutra.repository.ClienteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {
    @InjectMocks
    private ClienteService clienteService;

    @Mock
    ClienteRepository clienteRepository;

    @Mock
    private ViaCepClient viaCepClient;

    @Mock
    private RoleService roleService;

    @Test
    void ConsultarCepSave() {
        String cep = "123456789";
        ClienteRequest request = new ClienteRequest("Gabriel", "12345678901", "45999999999", cep);

        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setCep(cep);

        Assertions.assertEquals(cep, clienteEntity.getCep());
    }

    @Test
    void SalvarCliente() {
        ClienteRequest clienteRequest = new ClienteRequest("Gabriel", "12345678901", "45999999999", "85876113");
        Usuario usuario = new Usuario(1L, "Gabriel", "12345678", 21, Role.ADMIN, StatusOperacional.EM_ROTA, null);

        Assertions.assertEquals(clienteRequest, usuario);
    }


    @Test
    void ConsultarCepGet() {

    }

    @Test
    void DeletarCliente() {

    }
}
