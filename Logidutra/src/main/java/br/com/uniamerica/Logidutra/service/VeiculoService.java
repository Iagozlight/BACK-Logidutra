package br.com.uniamerica.Logidutra.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import br.com.uniamerica.Logidutra.controller.dto.VeiculoRequest;
import br.com.uniamerica.Logidutra.entity.VeiculoEntity;
import br.com.uniamerica.Logidutra.repository.VeiculoRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VeiculoService {
    private VeiculoRepository veiculoRepository;

    public boolean validarPlaca(String placa){
        final String regexPlaca = "^[A-Z]{3}[0-9]{1}[A-Z]{1}[0-9]{2}$";
        
        Pattern pattern = Pattern.compile(regexPlaca);
        Matcher matcher = pattern.matcher(placa);
        return matcher.find();
    }

    public VeiculoEntity salvar(VeiculoRequest veiculoRequest){
        String placa = veiculoRequest.placa();

        if(!validarPlaca(placa)){
            throw new IllegalArgumentException();
        }

        VeiculoEntity veiculo = new VeiculoEntity();

        veiculo.setMarca(veiculoRequest.marca());
        veiculo.setModelo(veiculoRequest.modelo());
        veiculo.setPlaca(veiculoRequest.placa());

        veiculo = veiculoRepository.save(veiculo);
        return veiculo;
    }

    public VeiculoEntity buscarPorId(Long id){
        VeiculoEntity veiculo = veiculoRepository.findById(id).orElseThrow();
        return veiculo;
    }

    public List<VeiculoEntity> listar(){
        return veiculoRepository.findAll();
    }

    public VeiculoEntity atualizar(Long id, VeiculoRequest veiculoRequest){
        VeiculoEntity veiculo = this.buscarPorId(id);

        veiculo.setMarca(veiculoRequest.marca());
        veiculo.setModelo(veiculoRequest.modelo());

        if(!validarPlaca(veiculoRequest.placa())) throw new IllegalArgumentException();
        veiculo.setPlaca(veiculoRequest.placa());

        veiculo = veiculoRepository.save(veiculo);

        return veiculo;
    }

    public VeiculoEntity atualizarParcial(Long id, VeiculoRequest veiculoRequest){
        VeiculoEntity veiculo = this.buscarPorId(id);

        if(veiculoRequest.marca() != null) veiculo.setMarca(veiculoRequest.marca());
        if(veiculoRequest.modelo() != null) veiculo.setModelo(veiculoRequest.modelo());
        if(veiculoRequest.placa() != null){
            if(!validarPlaca(veiculoRequest.placa())) throw new IllegalArgumentException();
            veiculo.setPlaca(veiculoRequest.placa());
        }

        veiculo = veiculoRepository.save(veiculo);

        return veiculo;
    }

    public void deletar(Long id){
        VeiculoEntity veiculo = this.buscarPorId(id);
        veiculoRepository.delete(veiculo);
    }
}
