package br.com.uniamerica.Logidutra.service;

import br.com.uniamerica.Logidutra.controller.dto.RomaneiosRequest;
import br.com.uniamerica.Logidutra.entity.ClienteEntity;
import br.com.uniamerica.Logidutra.entity.Romaneios;
import br.com.uniamerica.Logidutra.entity.Usuario;
import br.com.uniamerica.Logidutra.entity.VeiculoEntity;
import br.com.uniamerica.Logidutra.repository.ClienteRepository;
import br.com.uniamerica.Logidutra.repository.ProdutoRepository;
import br.com.uniamerica.Logidutra.repository.RomaneiosRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RomaneiosServiceTest {

    @Autowired
    RomaneiosService romaneiosService;

    @MockitoBean
    RomaneiosRepository romaneiosRepository;

    @MockitoBean
    VeiculoService veiculoService;

    @MockitoBean
    UsuarioService usuarioService;

    @MockitoBean
    ClienteRepository clienteRepository;

    @MockitoBean
    ProdutoRepository produtoRepository;

    @BeforeEach
    void setup() {
        VeiculoEntity veiculo = new VeiculoEntity();
        veiculo.setId(1L);

        Usuario motorista = new Usuario();
        motorista.setId(1L);

        ClienteEntity cliente = new ClienteEntity();
        cliente.setId(1L);

        when(veiculoService.buscarPorId(1L)).thenReturn(veiculo);
        when(usuarioService.buscarPorId(1L)).thenReturn(motorista);
        when(clienteRepository.findAllById(List.of(1L))).thenReturn(List.of(cliente));
    }

    @Test
    void cenario01_salvarRomaneioComDadosValidos(){

        Romaneios romaneioSalvo = new Romaneios();
        romaneioSalvo.setId(1L);

        when(romaneiosRepository.save(any())).thenReturn(romaneioSalvo);

        RomaneiosRequest request = new RomaneiosRequest(
                null,
                LocalDate.now(),
                1L,
                1L,
                List.of(1L),
                null
        );

        Romaneios resultado = romaneiosService.salvar(request);
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1L, resultado.getId());
        verify(romaneiosRepository).save(any(Romaneios.class));
    }

    @Test
    void cenario02_listarRomaneios(){
        Romaneios romaneios1 = new Romaneios();
        romaneios1.setId(1L);

        Romaneios romaneios2 = new Romaneios();
        romaneios2.setId(2L);

        List<Romaneios> romaneios = List.of(romaneios1, romaneios2);

        when(romaneiosRepository.findAll()).thenReturn(romaneios);

        List<Romaneios> resultado = romaneiosService.listar();

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(2, resultado.size());
        Assertions.assertEquals(1L, resultado.get(0).getId());
        Assertions.assertEquals(2L, resultado.get(1).getId());

        verify(romaneiosRepository).findAll();
    }

    @Test
    void cenario03_buscarRomaneioPorId(){
        Romaneios romaneio = new Romaneios();
        romaneio.setId(1L);

        when(romaneiosRepository.findById(1L))
                .thenReturn(java.util.Optional.of(romaneio));

        Romaneios resultado = romaneiosService.buscarPorId(1L);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1L, resultado.getId());

        verify(romaneiosRepository).findById(1L);
    }

    @Test
    void cenario04_buscarRomaneioPorIdInvalido(){

        when(romaneiosRepository.findById(99L))
                .thenReturn(java.util.Optional.empty());

        org.springframework.web.server.ResponseStatusException exception =
                Assertions.assertThrows(
                        org.springframework.web.server.ResponseStatusException.class,
                        () -> romaneiosService.buscarPorId(99L)
                );

        Assertions.assertEquals(
                org.springframework.http.HttpStatus.NOT_FOUND,
                exception.getStatusCode()
        );

        verify(romaneiosRepository).findById(99L);
    }

    @Test
    void cenario05_atualizarRomaneioComDadosValidos(){
        Romaneios romaneio = new Romaneios();
        romaneio.setId(1L);

        when(romaneiosRepository.findById(1L))
                .thenReturn(java.util.Optional.of(romaneio));

        Romaneios romaneioAtualizado = new Romaneios();
        romaneioAtualizado.setId(1L);

        when(romaneiosRepository.save(any(Romaneios.class)))
                .thenReturn(romaneioAtualizado);

        RomaneiosRequest request = new RomaneiosRequest(
                null,
                LocalDate.now(),
                1L,
                1L,
                List.of(1L),
                null
        );

        Romaneios resultado = romaneiosService.atualizar(1L, request);

        Assertions.assertNull(resultado);
        Assertions.assertEquals(1L, resultado.getId());

        verify(romaneiosRepository).findAllById(1L);
        verify(romaneiosRepository).save(any(Romaneios.class));
        verify(veiculoService).buscarPorId(1L);
        verify(usuarioService).buscarPorId(1L);
        verify(clienteRepository).findAllById(List.of(1L));

    }

    @Test
    void cenario06_atualizaParcialRomaneio(){

        Romaneios romaneio = new Romaneios();
        romaneio.setId(1L);

        when(romaneiosRepository.findById(1L))
                .thenReturn(java.util.Optional.of(romaneio));

        Romaneios romaneioAtualizado = new Romaneios();
        romaneioAtualizado.setId(1L);

        when(romaneiosRepository.save(any(Romaneios.class)))
                .thenReturn(romaneioAtualizado);

        LocalDate novaData = LocalDate.now();

        RomaneiosRequest request = new RomaneiosRequest(
                null,
                novaData,
                null,
                null,
                null,
                null
        );

        Romaneios resultado =
                romaneiosService.atualizarParcial(1L, request);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1L,resultado.getId());
        Assertions.assertEquals(novaData, romaneio.getData());

        verify(romaneiosRepository).findById(1L);
        verify(romaneiosRepository).save(any(Romaneios.class));

        verify(veiculoService, never()).buscarPorId(anyLong());
        verify(usuarioService, never()).buscarPorId(anyLong());
        verify(clienteRepository, never()).findAllById(anyList());
        verify(produtoRepository, never()).findAllById(anyList());
    }


    @Test
    void cenario07_deletarRomaneioComProdutos() {

        Romaneios romaneio = new Romaneios();
        romaneio.setId(1L);

        when(romaneiosRepository.findById(1L))
                .thenReturn(java.util.Optional.of(romaneio));

        romaneiosService.deletarPorID(1L);

        verify(romaneiosRepository).findById(1L);
        verify(produtoRepository, never()).deleteAll(anyList());
        verify(romaneiosRepository).delete(romaneio);
    }

}
