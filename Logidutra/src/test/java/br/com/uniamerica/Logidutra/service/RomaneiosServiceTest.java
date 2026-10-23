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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
}
