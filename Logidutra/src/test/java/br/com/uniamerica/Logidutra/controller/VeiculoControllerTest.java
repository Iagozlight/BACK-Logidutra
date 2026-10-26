package br.com.uniamerica.Logidutra.controller;

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

import br.com.uniamerica.logidutra.controller.VeiculoController;
import br.com.uniamerica.logidutra.controller.dto.VeiculoRequest;
import br.com.uniamerica.logidutra.controller.dto.VeiculoResponse;
import br.com.uniamerica.logidutra.entity.VeiculoEntity;
import br.com.uniamerica.logidutra.enums.StatusOperacional;
import br.com.uniamerica.logidutra.repository.VeiculoRepository;
import br.com.uniamerica.logidutra.service.VeiculoService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
    void VeiculoControllerSalvarRetornaBadRequestPlacaInvalida(){
        VeiculoEntity veiculo = new VeiculoEntity(1, "Fiat", "Uno", "ABC4F42", StatusOperacional.DISPONIVEL, null);
        when(veiculoRepository.save(any())).thenReturn(veiculo);

        ResponseEntity<VeiculoResponse> resposta = this.veiculoController.salvar(new VeiculoRequest("Fiat", "Uno", "a2c1d04"), 1L);

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
    }



    @Test
    void VeiculoControllerSalvarRetornaBadRequestComDadosInvalidados(){
        VeiculoEntity veiculo = new VeiculoEntity(1, "Fiat", "Uno", "ABC4F42", StatusOperacional.DISPONIVEL, null);
        when(veiculoRepository.save(any())).thenReturn(veiculo);

        ResponseEntity<VeiculoResponse> resposta = this.veiculoController.salvar(new VeiculoRequest("", "", ""), 1L);

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
    }

    @Test
    void VeiculoControllerListarRetornaOk(){
        List<VeiculoEntity> veiculos = List.of(new VeiculoEntity());
        when(veiculoRepository.findAll()).thenReturn(veiculos);

        ResponseEntity<List<VeiculoResponse>> resposta = this.veiculoController.listar();

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

    @Test
    void VeiculoControllerBuscarPorIdRetornaNotFoundComIdInexistente(){
        when(veiculoRepository.findById(anyLong())).thenThrow(NoSuchElementException.class);

        ResponseEntity<VeiculoResponse> resposta = this.veiculoController.buscarPorId(1L);

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void VeiculoControllerBuscarPorIdRetornaOk(){
        VeiculoEntity veiculo = new VeiculoEntity(1, "Fiat", "Uno", "ABC4F42", StatusOperacional.DISPONIVEL, null);
        when(veiculoRepository.findById(anyLong())).thenReturn(Optional.of(veiculo));

        ResponseEntity<VeiculoResponse> resposta = this.veiculoController.buscarPorId(1L);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

    @Test
    void VeiculoControllerAtualizarRetornaOk(){
        VeiculoEntity veiculo = new VeiculoEntity(1, "Fiat", "Uno", "ABC4F42", StatusOperacional.DISPONIVEL, null);
        when(veiculoRepository.findById(anyLong())).thenReturn(Optional.of(veiculo));
        when(veiculoRepository.save(any())).thenReturn(veiculo);

        ResponseEntity<VeiculoResponse> resposta = this.veiculoController.atualizarVeiculo(1L, new VeiculoRequest("Fiat", "Uno", "ABC4F42"), 1L);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

    @Test
    void VeiculoControllerAtualizarRetornaBadRequestComDadosInvalidos(){
        VeiculoEntity veiculo = new VeiculoEntity(1, "Fiat", "Uno", "ABC4F42", StatusOperacional.DISPONIVEL, null);
        when(veiculoRepository.findById(anyLong())).thenReturn(Optional.of(veiculo));
        when(veiculoRepository.save(any())).thenReturn(veiculo);

        ResponseEntity<VeiculoResponse> resposta = this.veiculoController.atualizarVeiculo(1L, new VeiculoRequest("", "", ""), 1L);

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
    }
    @Test
    void VeiculoControllerAtualizarRetornaBadRequestComPlacaInvalida(){
        VeiculoEntity veiculo = new VeiculoEntity(1, "Fiat", "Uno", "ABC4F42", StatusOperacional.DISPONIVEL, null);
        when(veiculoRepository.findById(anyLong())).thenReturn(Optional.of(veiculo));
        when(veiculoRepository.save(any())).thenReturn(veiculo);

        ResponseEntity<VeiculoResponse> resposta = this.veiculoController.atualizarVeiculo(1L, new VeiculoRequest("Fiat", "Uno", "a2c1d04"), 1L);

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
    }

    @Test
    void VeiculoControllerAtualizarRetornaNotFoundComIdInexistente(){
        when(veiculoRepository.findById(anyLong())).thenThrow(NoSuchElementException.class);

        ResponseEntity<VeiculoResponse> resposta = this.veiculoController.atualizarVeiculo(1L, new VeiculoRequest("Fiat", "Uno", "ABC4F42"), 1L);

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void VeiculoControllerAtualizarParcialRetornaOk(){
        VeiculoEntity veiculo = new VeiculoEntity(1, "Fiat", "Uno", "ABC4F42", StatusOperacional.DISPONIVEL, null);
        when(veiculoRepository.findById(anyLong())).thenReturn(Optional.of(veiculo));
        when(veiculoRepository.save(any())).thenReturn(veiculo);

        ResponseEntity<VeiculoResponse> resposta = this.veiculoController.atualizarParcialVeiculo(1L, new VeiculoRequest("Fiat", "Uno", "ABC4F42"), 1L);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

    @Test
    void VeiculoControllerAtualizarParcialRetornaBadRequestComDadosInvalidos(){
        VeiculoEntity veiculo = new VeiculoEntity(1, "Fiat", "Uno", "ABC4F42", StatusOperacional.DISPONIVEL, null);
        when(veiculoRepository.findById(anyLong())).thenReturn(Optional.of(veiculo));
        when(veiculoRepository.save(any())).thenReturn(veiculo);

        ResponseEntity<VeiculoResponse> resposta = this.veiculoController.atualizarParcialVeiculo(1L, new VeiculoRequest("", "", ""), 1L);

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
    }
    @Test
    void VeiculoControllerAtualizarParcialRetornaBadRequestComPlacaInvalida(){
        VeiculoEntity veiculo = new VeiculoEntity(1, "Fiat", "Uno", "ABC4F42", StatusOperacional.DISPONIVEL, null);
        when(veiculoRepository.findById(anyLong())).thenReturn(Optional.of(veiculo));
        when(veiculoRepository.save(any())).thenReturn(veiculo);

        ResponseEntity<VeiculoResponse> resposta = this.veiculoController.atualizarParcialVeiculo(1L, new VeiculoRequest("Fiat", "Uno", "a2c1d04"), 1L);

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
    }

    @Test
    void VeiculoControllerAtualizarParcialRetornaNotFoundComIdInexistente(){
        when(veiculoRepository.findById(anyLong())).thenThrow(NoSuchElementException.class);

        ResponseEntity<VeiculoResponse> resposta = this.veiculoController.atualizarParcialVeiculo(1L, new VeiculoRequest("Fiat", "Uno", "ABC4F42"), 1L);

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void VeiculoControllerMarcarDisponivelRetornaOk(){
        VeiculoEntity veiculo = new VeiculoEntity(1, "Fiat", "Uno", "ABC4F42", StatusOperacional.EM_ROTA, List.of());
        when(veiculoRepository.findById(anyLong())).thenReturn(Optional.of(veiculo));
        when(veiculoRepository.save(any())).thenReturn(veiculo);
        ResponseEntity<VeiculoResponse> resposta = this.veiculoController.marcarDisponivel(1L);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

    @Test
    void VeiculoControllerMarcarDisponivelRetornaNotFoundComIdInexistente(){
        when(veiculoRepository.findById(anyLong())).thenThrow(NoSuchElementException.class);

        ResponseEntity<VeiculoResponse> resposta = this.veiculoController.marcarDisponivel(1L);

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void VeiculoControllerMarcarEmRotaRetornaOk(){
        VeiculoEntity veiculo = new VeiculoEntity(1, "Fiat", "Uno", "ABC4F42", StatusOperacional.DISPONIVEL, List.of());
        when(veiculoRepository.findById(anyLong())).thenReturn(Optional.of(veiculo));
        when(veiculoRepository.save(any())).thenReturn(veiculo);

        ResponseEntity<VeiculoResponse> resposta = this.veiculoController.marcarEmRota(1L);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

    @Test
    void VeiculoControllerMarcarEmRotaRetornaNotFoundComIdInexistente(){
        when(veiculoRepository.findById(anyLong())).thenThrow(NoSuchElementException.class);

        ResponseEntity<VeiculoResponse> resposta = this.veiculoController.marcarEmRota(1L);

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void VeiculoControllerMarcarEmRotaRetornaConfict(){
        VeiculoEntity veiculo = new VeiculoEntity(1, "Fiat", "Uno", "ABC4F42", StatusOperacional.EM_ROTA, List.of());
        when(veiculoRepository.findById(anyLong())).thenReturn(Optional.of(veiculo));
        when(veiculoRepository.save(any())).thenReturn(veiculo);

        ResponseEntity<VeiculoResponse> resposta = this.veiculoController.marcarEmRota(1L);

        assertEquals(HttpStatus.CONFLICT, resposta.getStatusCode());
    }
    @Test
    void VeiculoControllerDeletarRetornaOk(){
        VeiculoEntity veiculo = new VeiculoEntity(1, "Fiat", "Uno", "ABC4F42", StatusOperacional.EM_ROTA, List.of());
        when(veiculoRepository.findById(anyLong())).thenReturn(Optional.of(veiculo));

        ResponseEntity<VeiculoResponse> resposta = this.veiculoController.deletar(1L,1L);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }
    @Test
    void VeiculoControllerDeletarRetornaNotFound(){
        VeiculoEntity veiculo = new VeiculoEntity(1, "Fiat", "Uno", "ABC4F42", StatusOperacional.EM_ROTA, List.of());
        when(veiculoRepository.findById(anyLong())).thenThrow(NoSuchElementException.class);

        ResponseEntity<VeiculoResponse> resposta = this.veiculoController.deletar(1L,1L);

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }
}
