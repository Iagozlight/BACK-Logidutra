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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class VeiculoServiceTest {
    @MockitoBean
    private VeiculoRepository veiculoRepository;
    @MockitoBean
    private RoleService roleService;

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

        verify(roleService).validarRole(anyLong(), any());

        VeiculoEntity veiculoSalvo = captorador.getValue();
        assertEquals("Fiat", veiculoSalvo.getMarca());
        assertEquals("Uno", veiculoSalvo.getModelo());
        assertEquals("ABC1C34", veiculoSalvo.getPlaca());
        assertEquals(StatusOperacional.DISPONIVEL, veiculoSalvo.getStatus());

        assertEquals(veiculoRetornado, resposta);
    }

    @Test
    void salvarNaoAceitaUsuarioSemPermissao(){
        doThrow(new RuntimeException()).when(roleService).validarRole(1L, Role.ADMIN);

        //Dados para teste
        VeiculoRequest veiculoRequest = new VeiculoRequest("Fiat", "Uno", "ABC1C34");
        Usuario usuario = new Usuario(1L, "Teste", "Teste", 20, Role.USER, StatusOperacional.DISPONIVEL, null);

        assertThrows(RuntimeException.class, () -> this.veiculoService.salvar(veiculoRequest, usuario));
        verify(veiculoRepository, never()).save(any());
    }
    @Test
    void salvarNaoAceitaPlacaInvalida(){
        //Dados para teste
        VeiculoRequest veiculoRequest = new VeiculoRequest("Fiat", "Uno", "a2c1d04");
        Usuario usuario = new Usuario(1L, "Teste", "Teste", 20, Role.ADMIN, StatusOperacional.DISPONIVEL, null);

        assertThrows(RuntimeException.class, () -> this.veiculoService.salvar(veiculoRequest, usuario));
        verify(veiculoRepository, never()).save(any());
    }

    @Test
    void atualizarComDadosValidos(){
        VeiculoEntity veiculo = new VeiculoEntity(1L, "Fiat", "Uno", "ABC1C34", StatusOperacional.DISPONIVEL, null);
        Usuario usuario = new Usuario(1L, "Teste", "Teste", 20, Role.ADMIN, StatusOperacional.DISPONIVEL, null);
        when(this.veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));

        VeiculoRequest veiculoRequest = new VeiculoRequest("Chevrolet", "Prisma", "DEF1G34");

        this.veiculoService.atualizar(1L, veiculoRequest, usuario);

        ArgumentCaptor<VeiculoEntity> capturador = ArgumentCaptor.forClass(VeiculoEntity.class);

        verify(roleService).validarRole(1L, Role.ADMIN);
        verify(veiculoRepository).findById(1L);
        verify(veiculoRepository).save(capturador.capture());

        VeiculoEntity veiculoAtualizado = capturador.getValue();

        assertEquals(veiculoRequest.marca(), veiculoAtualizado.getMarca());
        assertEquals(veiculoRequest.modelo(), veiculoAtualizado.getModelo());
        assertEquals(veiculoRequest.placa(), veiculoAtualizado.getPlaca());

    }

    @Test
    void atualizarRejeitaPlacaInvalida(){
        VeiculoEntity veiculo = new VeiculoEntity(1L, "Fiat", "Uno", "ABC1C34", StatusOperacional.DISPONIVEL, null);
        Usuario usuario = new Usuario(1L, "Teste", "Teste", 20, Role.ADMIN, StatusOperacional.DISPONIVEL, null);
        when(this.veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));

        VeiculoRequest veiculoRequest = new VeiculoRequest("Chevrolet", "Prisma", "a2c1d04");

        assertThrows(IllegalArgumentException.class, ()-> this.veiculoService.atualizar(1L, veiculoRequest, usuario));
        verify(veiculoRepository, never()).save(any());
    }

    @Test
    void atualizaNaoAceitaUsuarioSemPermissao(){
        doThrow(new RuntimeException()).when(roleService).validarRole(1L, Role.ADMIN);

        VeiculoEntity veiculo = new VeiculoEntity(1L, "Fiat", "Uno", "ABC1C34", StatusOperacional.DISPONIVEL, null);
        Usuario usuario = new Usuario(1L, "Teste", "Teste", 20, Role.USER, StatusOperacional.DISPONIVEL, null);
        when(this.veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));

        VeiculoRequest veiculoRequest = new VeiculoRequest("Chevrolet", "Prisma", "a2c1d04");

        assertThrows(RuntimeException.class, ()-> this.veiculoService.atualizar(1L, veiculoRequest, usuario));
        verify(veiculoRepository, never()).save(any());
    }

}
