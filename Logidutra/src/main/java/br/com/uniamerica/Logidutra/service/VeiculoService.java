package br.com.uniamerica.Logidutra.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.uniamerica.Logidutra.enums.StatusOperacional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.uniamerica.Logidutra.controller.dto.VeiculoRequest;
import br.com.uniamerica.Logidutra.entity.VeiculoEntity;
import br.com.uniamerica.Logidutra.repository.VeiculoRepository;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
@Slf4j
public class VeiculoService {
    private VeiculoRepository veiculoRepository;

    public boolean validarPlaca(String placa) {
        final String regexPlaca = "^[A-Z]{3}[0-9]{1}[A-Z]{1}[0-9]{2}$";

        Pattern pattern = Pattern.compile(regexPlaca);
        Matcher matcher = pattern.matcher(placa);
        return matcher.find();
    }

    @Transactional
    public VeiculoEntity salvar(VeiculoRequest veiculoRequest) {
        log.info("Salvando veículo placa {}", veiculoRequest.placa());

        if (!validarPlaca(veiculoRequest.placa())) {
            log.warn("Placa inválida: {}", veiculoRequest.placa());
            throw new IllegalArgumentException("Placa fora do formato esperado");
        }

        VeiculoEntity veiculo = new VeiculoEntity();

        veiculo.setMarca(veiculoRequest.marca());
        veiculo.setModelo(veiculoRequest.modelo());
        veiculo.setPlaca(veiculoRequest.placa());
        veiculo.setStatus(StatusOperacional.DISPONIVEL);

        return veiculoRepository.save(veiculo);
    }

    public VeiculoEntity buscarPorId(Long id) {
        return veiculoRepository.findById(id).orElseThrow();
    }

    public List<VeiculoEntity> listar() {
        return veiculoRepository.findAll();
    }

    @Transactional
    public VeiculoEntity atualizar(Long id, VeiculoRequest veiculoRequest) {
        VeiculoEntity veiculo = this.buscarPorId(id);

        veiculo.setMarca(veiculoRequest.marca());
        veiculo.setModelo(veiculoRequest.modelo());

        if (!validarPlaca(veiculoRequest.placa())) throw new IllegalArgumentException("Placa fora do formato esperado");
        veiculo.setPlaca(veiculoRequest.placa());

        return veiculoRepository.save(veiculo);
    }

    @Transactional
    public VeiculoEntity atualizarParcial(Long id, VeiculoRequest veiculoRequest) {
        VeiculoEntity veiculo = this.buscarPorId(id);

        if (veiculoRequest.marca() != null) veiculo.setMarca(veiculoRequest.marca());
        if (veiculoRequest.modelo() != null) veiculo.setModelo(veiculoRequest.modelo());
        if (veiculoRequest.placa() != null) {
            if (!validarPlaca(veiculoRequest.placa()))
                throw new IllegalArgumentException("Placa fora do formato esperado");
            veiculo.setPlaca(veiculoRequest.placa());
        }

        return veiculoRepository.save(veiculo);
    }

    @Transactional
    public void deletar(Long id) {
        log.info("Deletando veículo {}", id);
        VeiculoEntity veiculo = this.buscarPorId(id);
        veiculoRepository.delete(veiculo);
    }
}
