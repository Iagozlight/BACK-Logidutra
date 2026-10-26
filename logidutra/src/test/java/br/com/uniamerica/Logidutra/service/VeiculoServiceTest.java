package br.com.uniamerica.Logidutra.service;

import br.com.uniamerica.Logidutra.controller.dto.VeiculoRequest;
import br.com.uniamerica.Logidutra.entity.Usuario;
import br.com.uniamerica.Logidutra.entity.VeiculoEntity;
import br.com.uniamerica.Logidutra.enums.Role;
import br.com.uniamerica.Logidutra.enums.StatusOperacional;
import br.com.uniamerica.Logidutra.repository.VeiculoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VeiculoServiceTest {
    @Mock
    VeiculoRepository veiculoRepository;
    @Mock
    RoleService roleService;

    @InjectMocks
    VeiculoService veiculoService;

    @Test
    void rejeitaPlacaInvalida(){
        String placa = "a2c1d04";

        boolean resultado = this.veiculoService.validarPlaca(placa);
        assertFalse(resultado);
    }
    @Test
    void aceitaPlacaValida(){
        String placa = "ABC1C34";

        boolean resultado = this.veiculoService.validarPlaca(placa);
        assertTrue(resultado);
    }

    @Test
    void salvarVeiculoValido() {
        //Entidade mockada
        VeiculoEntity veiculoRetornado = new VeiculoEntity(1L, "Fiat", "Uno", "ABC1C34", StatusOperacional.DISPONIVEL, null);
        when(this.veiculoRepository.save(any(VeiculoEntity.class))).thenReturn(veiculoRetornado);

        //Dados para teste
        VeiculoRequest veiculoRequest = new VeiculoRequest("Fiat", "Uno", "ABC1C34");
        Usuario usuario = new Usuario(1L, "Teste", "Teste", 20, Role.ADMIN, StatusOperacional.DISPONIVEL, null);


        var carro = this.veiculoService.salvar(veiculoRequest, usuario);


        assertEquals("Fiat", carro.getMarca());
        assertEquals("Uno", carro.getModelo());
        assertEquals("ABC1C34", carro.getPlaca());
        assertEquals(StatusOperacional.DISPONIVEL, carro.getStatus());
    }

    @Test
    void salvarNaoSalvaQuandoValidacaoDeRoleFalha(){
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
        when(this.veiculoRepository.save(any(VeiculoEntity.class))).thenReturn(veiculo);
        when(this.veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));

        VeiculoRequest veiculoRequest = new VeiculoRequest("Chevrolet", "Prisma", "DEF1G34");

        var carro = this.veiculoService.atualizar(1L, veiculoRequest, usuario);

        assertEquals("Chevrolet", carro.getMarca());
        assertEquals("Prisma", carro.getModelo());
        assertEquals("DEF1G34", carro.getPlaca());
    }

    @Test
    void atualizarNaoSalvaQuandoPlacaInvalida(){
        VeiculoEntity veiculo = new VeiculoEntity(1L, "Fiat", "Uno", "ABC1C34", StatusOperacional.DISPONIVEL, null);
        Usuario usuario = new Usuario(1L, "Teste", "Teste", 20, Role.ADMIN, StatusOperacional.DISPONIVEL, null);
        when(this.veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));

        VeiculoRequest veiculoRequest = new VeiculoRequest("Chevrolet", "Prisma", "a2c1d04");

        assertThrows(IllegalArgumentException.class, ()-> this.veiculoService.atualizar(1L, veiculoRequest, usuario));
        verify(veiculoRepository, never()).save(any());
    }

    @Test
    void atualizarNaoSalvaQuandoValidacaoDeRoleFalha(){
        doThrow(new RuntimeException()).when(roleService).validarRole(1L, Role.ADMIN);

        VeiculoEntity veiculo = new VeiculoEntity(1L, "Fiat", "Uno", "ABC1C34", StatusOperacional.DISPONIVEL, null);
        Usuario usuario = new Usuario(1L, "Teste", "Teste", 20, Role.ADMIN, StatusOperacional.DISPONIVEL, null);

        VeiculoRequest veiculoRequest = new VeiculoRequest("Chevrolet", "Prisma", "ABC1C34");

        assertThrows(RuntimeException.class, ()-> this.veiculoService.atualizar(1L, veiculoRequest, usuario));
        verify(veiculoRepository, never()).save(any());
    }

    @Test
    void atualizarParcialVeiculoValido() {
        VeiculoEntity veiculo = new VeiculoEntity(1L, "Fiat", "Uno", "ABC1C34", StatusOperacional.DISPONIVEL, null);
        Usuario usuario = new Usuario(1L, "Teste", "Teste", 20, Role.ADMIN, StatusOperacional.DISPONIVEL, null);
        when(this.veiculoRepository.save(any(VeiculoEntity.class))).thenReturn(veiculo);
        when(this.veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));

        VeiculoRequest veiculoRequest = new VeiculoRequest("Chevrolet", "Prisma", "DEF1G34");

        var carro = this.veiculoService.atualizarParcial(1L, veiculoRequest, usuario);

        assertEquals(veiculoRequest.marca(), carro.getMarca());
        assertEquals(veiculoRequest.modelo(), carro.getModelo());
        assertEquals(veiculoRequest.placa(), carro.getPlaca());
    }

    @Test
    void atualizarParcialVeiculoValidoComRequestParcialMarca() {
        VeiculoEntity veiculo = new VeiculoEntity(1L, "Fiat", "Uno", "ABC1C34", StatusOperacional.DISPONIVEL, null);
        Usuario usuario = new Usuario(1L, "Teste", "Teste", 20, Role.ADMIN, StatusOperacional.DISPONIVEL, null);
        when(this.veiculoRepository.save(any(VeiculoEntity.class))).thenReturn(veiculo);
        when(this.veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));

        VeiculoRequest veiculoRequest = new VeiculoRequest("Chevrolet", null, null);

        //Marca
        var carro = this.veiculoService.atualizarParcial(1L, veiculoRequest, usuario);

        assertEquals(veiculoRequest.marca(), carro.getMarca());
        assertEquals("Uno", carro.getModelo());
        assertEquals("ABC1C34", carro.getPlaca());
    }

    @Test
    void atualizarParcialVeiculoValidoComRequestParcialModelo(){
        VeiculoEntity veiculo = new VeiculoEntity(1L, "Fiat", "Uno", "ABC1C34", StatusOperacional.DISPONIVEL, null);
        Usuario usuario = new Usuario(1L, "Teste", "Teste", 20, Role.ADMIN, StatusOperacional.DISPONIVEL, null);
        when(this.veiculoRepository.save(any(VeiculoEntity.class))).thenReturn(veiculo);
        when(this.veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));

        VeiculoRequest veiculoRequest = new VeiculoRequest(null, "Prisma", null);

        //Modelo
        var carro = this.veiculoService.atualizarParcial(1L, veiculoRequest, usuario);

        assertEquals("Fiat", carro.getMarca());
        assertEquals(veiculoRequest.modelo(), carro.getModelo());
        assertEquals("ABC1C34", carro.getPlaca());
    }

    @Test
    void atualizarParcialVeiculoValidoComRequestParcialPlaca(){
        VeiculoEntity veiculo = new VeiculoEntity(1L, "Fiat", "Uno", "ABC1C34", StatusOperacional.DISPONIVEL, null);
        Usuario usuario = new Usuario(1L, "Teste", "Teste", 20, Role.ADMIN, StatusOperacional.DISPONIVEL, null);
        when(this.veiculoRepository.save(any(VeiculoEntity.class))).thenReturn(veiculo);
        when(this.veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));

        VeiculoRequest veiculoRequest = new VeiculoRequest(null, null, "DEF1G34");

        //Placa
        var carro = this.veiculoService.atualizarParcial(1L, veiculoRequest, usuario);


        assertEquals("Fiat", carro.getMarca());
        assertEquals("Uno", carro.getModelo());
        assertEquals(veiculoRequest.placa(), carro.getPlaca());
    }

    @Test
    void atualizarParcialNaoSalvaQuandoPlacaInvalida(){
        VeiculoEntity veiculo = new VeiculoEntity(1L, "Fiat", "Uno", "ABC1C34", StatusOperacional.DISPONIVEL, null);
        Usuario usuario = new Usuario(1L, "Teste", "Teste", 20, Role.ADMIN, StatusOperacional.DISPONIVEL, null);
        when(this.veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));

        VeiculoRequest veiculoRequest = new VeiculoRequest(null, null, "a2c1d04");

        assertThrows(IllegalArgumentException.class, ()-> veiculoService.atualizarParcial(1L, veiculoRequest, usuario));
        verify(veiculoRepository, never()).save(any());
    }

    @Test
    void atualizarParcialNaoSalvaQuandoValidacaoDeRoleFalha(){
        Usuario usuario = new Usuario(1L, "Teste", "Teste", 20, Role.ADMIN, StatusOperacional.DISPONIVEL, null);
        doThrow(new RuntimeException()).when(roleService).validarRole(1L, Role.ADMIN);

        VeiculoRequest veiculoRequest = new VeiculoRequest(null, null, "DEF1G34");

        assertThrows(RuntimeException.class, ()-> veiculoService.atualizarParcial(1L, veiculoRequest, usuario));
        verify(veiculoRepository, never()).save(any());
    }
    @Test
    void atualizarParcialNaoSalvaQuandoFindByIdFalha(){
        Usuario usuario = new Usuario(1L, "Teste", "Teste", 20, Role.ADMIN, StatusOperacional.DISPONIVEL, null);
        when(this.veiculoRepository.findById(1L)).thenReturn(Optional.empty());

        VeiculoRequest veiculoRequest = new VeiculoRequest(null, null, "DEF1G34");

        assertThrows(NoSuchElementException.class, ()-> veiculoService.atualizarParcial(1L, veiculoRequest, usuario));
        verify(veiculoRepository, never()).save(any());
    }

    @Test
    void deletarVeiculoValido(){
        VeiculoEntity veiculo = new VeiculoEntity(1L, "Fiat", "Uno", "ABC1C34", StatusOperacional.DISPONIVEL, null);
        Usuario usuario = new Usuario(1L, "Teste", "Teste", 20, Role.ADMIN, StatusOperacional.DISPONIVEL, null);
        when(this.veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));

        this.veiculoService.deletar(1L, usuario);

        verify(veiculoRepository).delete(veiculo);
    }
    @Test
    void deletarNaoDeletaQuandoNaoAchaVeiculo(){
        Usuario usuario = new Usuario(1L, "Teste", "Teste", 20, Role.ADMIN, StatusOperacional.DISPONIVEL, null);
        when(this.veiculoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> this.veiculoService.deletar(1L, usuario));

        verify(veiculoRepository, never()).delete(any());
    }
    @Test
    void deletarNaoDeletaQuandoValidarUsuarioFalha(){
        Usuario usuario = new Usuario(1L, "Teste", "Teste", 20, Role.ADMIN, StatusOperacional.DISPONIVEL, null);
        doThrow(new RuntimeException()).when(roleService).validarRole(1L, Role.ADMIN);

        assertThrows(RuntimeException.class, () -> this.veiculoService.deletar(1L, usuario));

        verify(veiculoRepository, never()).delete(any());
    }

    @Test
    void marcarEmRotaQuandoValido(){
        VeiculoEntity veiculo = new VeiculoEntity(1L, "Fiat", "Uno", "ABC1C34", StatusOperacional.DISPONIVEL, null);
        when(this.veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));

        this.veiculoService.marcarEmRota(1L);

        assertEquals(StatusOperacional.EM_ROTA, veiculo.getStatus());
        verify(veiculoRepository).save(veiculo);
    }

    @Test
    void marcarEmRotaNaoSalvaSeVeiculoJaEmRota(){
        VeiculoEntity veiculo = new VeiculoEntity(1L, "Fiat", "Uno", "ABC1C34", StatusOperacional.EM_ROTA, null);
        when(this.veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));


        assertThrows(ResponseStatusException.class, () -> this.veiculoService.marcarEmRota(1L));

        verify(veiculoRepository, never()).save(any());
    }

    @Test
    void marcarDisponivelQuandoValido(){
        VeiculoEntity veiculo = new VeiculoEntity(1L, "Fiat", "Uno", "ABC1C34", StatusOperacional.EM_ROTA, null);
        when(this.veiculoRepository.save(any(VeiculoEntity.class))).thenReturn(veiculo);
        when(this.veiculoRepository.findById(1L)).thenReturn(Optional.of(veiculo));

        var carro = this.veiculoService.marcarDisponivel(1L);

        assertEquals(StatusOperacional.DISPONIVEL, carro.getStatus());
    }
}
