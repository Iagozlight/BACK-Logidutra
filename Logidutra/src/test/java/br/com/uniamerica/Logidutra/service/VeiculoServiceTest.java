package br.com.uniamerica.Logidutra.service;

import br.com.uniamerica.Logidutra.controller.dto.VeiculoRequest;
import br.com.uniamerica.Logidutra.entity.Usuario;
import br.com.uniamerica.Logidutra.entity.VeiculoEntity;
import br.com.uniamerica.Logidutra.enums.Role;
import br.com.uniamerica.Logidutra.enums.StatusOperacional;
import br.com.uniamerica.Logidutra.repository.VeiculoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class VeiculoServiceTest {
    @MockitoBean
    private VeiculoRepository veiculoRepository;

    @Autowired
    private VeiculoService veiculoService;

    @Test
    void rejeitaPlacaInvalida(){
        String placa = "a2c1d04";

        boolean resultado = this.veiculoService.validarPlaca(placa);
        assertEquals(false,resultado);
    }
    @Test
    void aceitaPlacaValida(){
        String placa = "ABC1C34";

        boolean resultado = this.veiculoService.validarPlaca(placa);
        assertEquals(true,resultado);
    }

    @Test
    void salvarVeiculoValido() {
        //Entidade mockada
        VeiculoEntity veiculoRetornado = new VeiculoEntity(1L, "Fiat", "Uno", "ABC1C34", StatusOperacional.DISPONIVEL, null);
        when(this.veiculoRepository.save(any(VeiculoEntity.class))).thenReturn(veiculoRetornado);

        //Dados para teste
        VeiculoRequest veiculoRequest = new VeiculoRequest("Fiat", "Uno", "ABC1C34");
        Usuario usuario = new Usuario(1L, "Teste", "Teste", 20, Role.ADMIN, StatusOperacional.DISPONIVEL, null);


        VeiculoEntity resposta = this.veiculoService.salvar(veiculoRequest, usuario);


        ArgumentCaptor<VeiculoEntity> captorador = ArgumentCaptor.forClass(VeiculoEntity.class);

        verify(veiculoRepository).save(captorador.capture());

        VeiculoEntity veiculoSalvo = captorador.getValue();
        assertEquals("Fiat", veiculoSalvo.getMarca());
        assertEquals("Uno", veiculoSalvo.getModelo());
        assertEquals("ABC1C34", veiculoSalvo.getPlaca());

        assertEquals(veiculoRetornado, resposta);
    }
}
