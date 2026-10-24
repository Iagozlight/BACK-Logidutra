package br.com.uniamerica.Logidutra.controller;

import br.com.uniamerica.Logidutra.controller.dto.RomaneiosRequest;
import br.com.uniamerica.Logidutra.controller.dto.RomaneiosResponse;
import br.com.uniamerica.Logidutra.entity.Romaneios;
import br.com.uniamerica.Logidutra.service.RomaneiosService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RomaneiosControllerTest {

    @Autowired
    RomaneiosController romaneiosController;

    @MockitoBean
    RomaneiosService romaneiosService;

    @Test
    void cenario01_salvarRomaneioRetornaCreated(){

        Romaneios romaneio = new Romaneios();
        romaneio.setId(1L);

        when(romaneiosService.salvar(any(RomaneiosRequest.class)))
                .thenReturn(romaneio);

        RomaneiosRequest request = new RomaneiosRequest(
                null,
                LocalDate.now(),
                1L,
                1l,
                List.of(1L),
                null
        );

        ResponseEntity<RomaneiosResponse> resposta =
                romaneiosController.salvar(request);

        Assertions.assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        verify(romaneiosService).salvar(request);
    }

    @Test
    void cenario02_listarRomaneiosRetornaOk(){
        Romaneios romaneio = new Romaneios();
        romaneio.setId(1L);

        when(romaneiosService.listar())
                .thenReturn(List.of(romaneio));

        ResponseEntity<List<RomaneiosResponse>> resposta =
                romaneiosController.listar();

        Assertions.assertEquals(HttpStatus.OK, resposta.getStatusCode());
        Assertions.assertNotNull(resposta.getBody());
        Assertions.assertEquals(1, resposta.getBody().size());

        verify(romaneiosService).listar();
    }

    @Test
    void cenario03_buscarRomaneioPorIdRetornaOk(){

        Romaneios romaneio = new Romaneios();
        romaneio.setId(1L);

        when(romaneiosService.buscarPorId(1L))
                .thenReturn(romaneio);

        ResponseEntity<RomaneiosResponse> resposta =
                romaneiosController.buscarPorId(1L);

        Assertions.assertEquals(HttpStatus.OK, resposta.getStatusCode());

        verify(romaneiosService).buscarPorId(1L)
    }
}
