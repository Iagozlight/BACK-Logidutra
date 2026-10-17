package br.com.uniamerica.Logidutra.service;

import br.com.uniamerica.Logidutra.controller.dto.ClienteRequest;
import br.com.uniamerica.Logidutra.data.feign.ViaCepClient;
import br.com.uniamerica.Logidutra.repository.ClienteRepository;
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
        String cep = "123-456-7890";
        ClienteRequest request = new ClienteRequest("Gabriel", "12345678901", "45999999999", cep);
    }
}
