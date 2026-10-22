package br.com.uniamerica.Logidutra.controller;

import br.com.uniamerica.Logidutra.controller.dto.VeiculoRequest;
import br.com.uniamerica.Logidutra.controller.dto.VeiculoResponse;
import br.com.uniamerica.Logidutra.entity.VeiculoEntity;
import br.com.uniamerica.Logidutra.enums.StatusOperacional;
import br.com.uniamerica.Logidutra.repository.VeiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@SpringJUnitWebConfig
public class VeiculoControllerTest {
    @MockitoBean
    VeiculoRepository veiculoRepository;
    @Autowired
    VeiculoController veiculoController;


    @Test
    void VeiculoControllerSalvarRetornaStatusCreated(){
        VeiculoEntity veiculo = new VeiculoEntity(1, "Fiat", "Uno", "ABC4F42", StatusOperacional.DISPONIVEL, null);
        when(veiculoRepository.save(any())).thenReturn(veiculo);

        ResponseEntity<VeiculoResponse> resposta = this.veiculoController.salvar(new VeiculoRequest("Fiat", "Uno", "ABC4F42"), 1L);

        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
    }

    @Test
    void VeiculoControllerRetornaBadRequestPlacaInvalida(){
        VeiculoEntity veiculo = new VeiculoEntity(1, "Fiat", "Uno", "ABC4F42", StatusOperacional.DISPONIVEL, null);
        when(veiculoRepository.save(any())).thenReturn(veiculo);

        ResponseEntity<VeiculoResponse> resposta = this.veiculoController.salvar(new VeiculoRequest("Fiat", "Uno", "a2c1d04"), 1L);

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
    }

    @Test
    void VeiculoControllerRetornaBadRequestComDadosInvalidados(){
        VeiculoEntity veiculo = new VeiculoEntity(1, "Fiat", "Uno", "ABC4F42", StatusOperacional.DISPONIVEL, null);
        when(veiculoRepository.save(any())).thenReturn(veiculo);

        ResponseEntity<VeiculoResponse> resposta = this.veiculoController.salvar(new VeiculoRequest("", "", ""), 1L);

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
    }
}
