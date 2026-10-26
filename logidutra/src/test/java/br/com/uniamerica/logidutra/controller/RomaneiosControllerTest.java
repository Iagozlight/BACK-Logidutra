package br.com.uniamerica.logidutra.controller;

import br.com.uniamerica.logidutra.controller.dto.RomaneiosRequest;
import br.com.uniamerica.logidutra.controller.dto.RomaneiosResponse;
import br.com.uniamerica.logidutra.entity.ClienteEntity;
import br.com.uniamerica.logidutra.entity.Romaneios;
import br.com.uniamerica.logidutra.entity.Usuario;
import br.com.uniamerica.logidutra.entity.VeiculoEntity;
import br.com.uniamerica.logidutra.service.RomaneiosService;
import br.com.uniamerica.logidutra.service.UsuarioService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RomaneiosControllerTest {

    @Mock
    private RomaneiosService romaneiosService;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private RomaneiosController romaneiosController;

    @Test
    void cenario01_salvarRomaneioRetornaCreated(){

        Romaneios romaneio = criarRomaneio();

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
        Romaneios romaneio = criarRomaneio();

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

        Romaneios romaneio = criarRomaneio();

        when(romaneiosService.buscarPorId(1L))
                .thenReturn(romaneio);

        ResponseEntity<RomaneiosResponse> resposta =
                romaneiosController.buscarPorId(1L);

        Assertions.assertEquals(HttpStatus.OK, resposta.getStatusCode());

        verify(romaneiosService).buscarPorId(1L);
    }

    @Test
    void cenario04_RetornoatualizarRomaneios(){

        Romaneios romaneio = criarRomaneio();

        when(romaneiosService.atualizar(
                eq(1L), any(RomaneiosRequest.class)))
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
                romaneiosController.atualizar(1L, request);

        Assertions.assertEquals(HttpStatus.OK, resposta.getStatusCode());

        verify(romaneiosService).atualizar(1L, request);
    }

    @Test
    void cenario05_RetornoAtualizarParcial(){

        Romaneios romaneio = criarRomaneio();

        when(romaneiosService.atualizarParcial(
                eq(1L), any(RomaneiosRequest.class)))
                .thenReturn(romaneio);

        RomaneiosRequest request = new RomaneiosRequest(
                null,
                LocalDate.now(),null, null, null, null);

        ResponseEntity<RomaneiosResponse> resposta =
                romaneiosController.atualizarParcial(1L, request);

        Assertions.assertEquals(HttpStatus.OK, resposta.getStatusCode());

        verify(romaneiosService).atualizarParcial(1L, request);
    }

    @Test
    void cenario06_RetornoNoContentRomaneio(){
        doNothing().when(romaneiosService).deletarPorID(1L);

        ResponseEntity<?> resposta =
                romaneiosController.deletar(1L);

        Assertions.assertEquals(
                HttpStatus.NO_CONTENT,
                resposta.getStatusCode()
        );

        verify(romaneiosService).deletarPorID(1L);
    }

    private Romaneios criarRomaneio() {
        Romaneios romaneio = new Romaneios();
        romaneio.setId(1L);

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        romaneio.setUsuario(usuario);

        VeiculoEntity veiculo = new VeiculoEntity();
        veiculo.setId(1L);
        romaneio.setVeiculo(veiculo);

        ClienteEntity cliente = new ClienteEntity();
        cliente.setId(1L);
        romaneio.setClientes(List.of(cliente));
        romaneio.setProdutoList(List.of());
        return romaneio;
    }
    @Test
    void cenario07_salvarRomaneioRetornaBadRequest() {
        RomaneiosRequest request = new RomaneiosRequest(
                null,
                LocalDate.now(),
                1L,
                1L,
                List.of(1L),
                null
        );
        when(romaneiosService.salvar(request)).thenThrow(new RuntimeException());

        ResponseEntity<RomaneiosResponse> resposta = romaneiosController.salvar(request);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        verify(romaneiosService).salvar(request);
    }

    @Test
    void cenario08_listarRomaneiosVazio() {
        when(romaneiosService.listar()).thenReturn(List.of());

        ResponseEntity<List<RomaneiosResponse>> resposta = romaneiosController.listar();

        Assertions.assertEquals(HttpStatus.OK, resposta.getStatusCode());
        Assertions.assertNotNull(resposta.getBody());
        Assertions.assertTrue(resposta.getBody().isEmpty());
    }

    @Test
    void cenario09_listarRomaneiosRetornaBadRequest() {
        when(romaneiosService.listar()).thenThrow(new RuntimeException());

        ResponseEntity<List<RomaneiosResponse>> resposta = romaneiosController.listar();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
    }

    @Test
    void cenario10_buscarRomaneioRetornaBadRequest() {
        when(romaneiosService.buscarPorId(99L)).thenThrow(new RuntimeException());

        ResponseEntity<RomaneiosResponse> resposta = romaneiosController.buscarPorId(99L);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        verify(romaneiosService).buscarPorId(99L);
    }

    @Test
    void cenario11_atualizarRomaneioRetornaBadRequest() {
        RomaneiosRequest request = new RomaneiosRequest(
                null,
                LocalDate.now(),
                1L,
                1L,
                List.of(1L),
                null
        );
        when(romaneiosService.atualizar(1L, request)).thenThrow(new RuntimeException());

        ResponseEntity<RomaneiosResponse> resposta = romaneiosController.atualizar(1L, request);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        verify(romaneiosService).atualizar(1L, request);
    }

    @Test
    void cenario12_atualizarParcialRetornaBadRequest() {
        RomaneiosRequest request = new RomaneiosRequest(null, null, null, null, null, null);
        when(romaneiosService.atualizarParcial(1L, request)).thenThrow(new RuntimeException());

        ResponseEntity<RomaneiosResponse> resposta = romaneiosController.atualizarParcial(1L, request);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        verify(romaneiosService).atualizarParcial(1L, request);
    }

    @Test
    void cenario13_deletarRomaneioRetornaBadRequest() {
        doThrow(new RuntimeException()).when(romaneiosService).deletarPorID(99L);

        ResponseEntity<?> resposta = romaneiosController.deletar(99L);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
        verify(romaneiosService).deletarPorID(99L);
    }
}
