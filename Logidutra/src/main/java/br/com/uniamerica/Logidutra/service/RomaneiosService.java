package br.com.uniamerica.Logidutra.service;

import br.com.uniamerica.Logidutra.controller.dto.ProdutoRequest;
import br.com.uniamerica.Logidutra.controller.dto.RomaneiosRequest;
import br.com.uniamerica.Logidutra.controller.dto.RomaneiosResponse;
import br.com.uniamerica.Logidutra.entity.*;
import br.com.uniamerica.Logidutra.repository.ClienteRepository;
import br.com.uniamerica.Logidutra.repository.ProdutoRepository;
import br.com.uniamerica.Logidutra.repository.RomaneiosRepository;
import jakarta.persistence.Entity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
@Slf4j
public class RomaneiosService {

    private final RomaneiosRepository romaneiosRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final VeiculoService veiculoService;
    private final UsuarioService usuarioService;


    @Transactional
    public Romaneios salvar(RomaneiosRequest romaneiosRequest){

        log.info("Criando romaneio para data {}", romaneiosRequest.data());

        VeiculoEntity veiculo = veiculoService.buscarPorId(romaneiosRequest.veiculoId());
        Usuario motorista = usuarioService.buscarPorId(romaneiosRequest.usuarioId());

        List<ClienteEntity> clientes = clienteRepository.findAllById(romaneiosRequest.clienteId());
        if (clientes.size() != romaneiosRequest.clienteId().size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Um ou mais clientes informados não existem");
        }

        Romaneios romaneios = new Romaneios();
        romaneios.setData(romaneiosRequest.data());
        romaneios.setVeiculo(veiculo);
        romaneios.setUsuario(motorista);
        romaneios.setClientes(clientes);

        if (romaneiosRequest.produtoId() != null && !romaneiosRequest.produtoId().isEmpty()) {
            List<Produto> produtos = produtoRepository.findAllById(romaneiosRequest.produtoId());
            if (produtos.size() != romaneiosRequest.produtoId().size()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Um ou mais produtos informados não existem");
            }
            romaneios.setProdutoList(produtos);
        }

        Romaneios salvo = romaneiosRepository.save(romaneios);
        log.info("Romaneio {} criado", salvo.getId());
        return salvo;
    }

    public List <Romaneios> listar (){
        return this.romaneiosRepository.findAll();
    }

    public Romaneios buscarPorId (long id){
        return this.romaneiosRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Romaneio " + id + " não encontrado")
                );
    }

    @Transactional
    public Romaneios atualizar (long id, RomaneiosRequest romaneiosRequest) {
        Romaneios romaneios = this.buscarPorId(id);

        romaneios.setData(romaneiosRequest.data());

        romaneios.setVeiculo(veiculoService.buscarPorId(romaneiosRequest.veiculoId()));
        romaneios.setUsuario(usuarioService.buscarPorId(romaneiosRequest.usuarioId()));
        romaneios.setClientes(clienteRepository.findAllById(romaneiosRequest.clienteId()));

        if (romaneiosRequest.produtoId() != null) {
            romaneios.setProdutoList(produtoRepository.findAllById(romaneiosRequest.produtoId()));
        }

        return romaneiosRepository.save(romaneios);
    }

    @Transactional
    public Romaneios atualizarParcial (long id, RomaneiosRequest romaneiosRequest){
        Romaneios romaneios = this.buscarPorId(id);

        if (romaneiosRequest.data() != null) romaneios.setData(romaneiosRequest.data());
        if (romaneiosRequest.veiculoId() != null) romaneios.setVeiculo(veiculoService.buscarPorId(romaneiosRequest.veiculoId()));
        if (romaneiosRequest.usuarioId() != null) romaneios.setUsuario(usuarioService.buscarPorId(romaneiosRequest.usuarioId()));
        if (romaneiosRequest.clienteId() != null) romaneios.setClientes(clienteRepository.findAllById(romaneiosRequest.clienteId()));
        if (romaneiosRequest.produtoId() != null) romaneios.setProdutoList(produtoRepository.findAllById(romaneiosRequest.produtoId()));

        return romaneiosRepository.save(romaneios);
    }

    @Transactional
    public void deletarPorID(long id){
        this.buscarPorId(id);
        this.romaneiosRepository.deleteById(id);
    }
}
