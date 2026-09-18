package br.com.uniamerica.Logidutra.service;

import br.com.uniamerica.Logidutra.controller.dto.ClienteRequest;
import br.com.uniamerica.Logidutra.controller.dto.ClienteResponse;
import br.com.uniamerica.Logidutra.controller.dto.ViaCepResponse;
import br.com.uniamerica.Logidutra.data.feign.ViaCepClient;
import br.com.uniamerica.Logidutra.entity.ClienteEntity;
import br.com.uniamerica.Logidutra.entity.Usuario;
import br.com.uniamerica.Logidutra.entity.VeiculoEntity;
import br.com.uniamerica.Logidutra.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ClienteService {

    private final Logger logger;

    @Autowired
    private ClienteRepository clienteRepository;

    private final ViaCepClient viaCepClient;

    private ViaCepResponse consultarCep(String cep) {
        log.info("Consultando CEP {} na ViaCEP", cep);
        ViaCepResponse viaCepResponse = viaCepClient.buscarPorCep(cep);

        if (viaCepResponse == null || Boolean.TRUE.equals(viaCepResponse.erro())) {
            log.warn("CEP {} não encontrado na ViaCEP", cep);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CEP não encontrado: " + cep);
        }

        return viaCepResponse;
    }

    @Transactional
    public ClienteEntity salvar(ClienteRequest clienteRequest) {
        ClienteEntity clienteEntity = new ClienteEntity();

        clienteEntity.setNome(clienteRequest.nome());
        clienteEntity.setCpf(clienteRequest.cpf());
        clienteEntity.setTelefone(clienteRequest.telefone());
        clienteEntity.setCep(clienteRequest.cep());

        return clienteRepository.save(clienteEntity);
    }

    public List<ClienteEntity> listar() {
        return this.clienteRepository.findAll();
    }

    public ClienteEntity buscarPorId(Long id) {
        ClienteEntity clienteEntity = clienteRepository.findById(id).orElseThrow();
        return clienteEntity;
    }

    @Transactional
    public ClienteEntity atualizar(Long id, ClienteRequest clienteRequest) {
        ClienteEntity clienteEntity = this.buscarPorId(id);

        clienteEntity.setNome(clienteRequest.nome());
        clienteEntity.setCpf(clienteRequest.cpf());
        clienteEntity.setTelefone(clienteRequest.telefone());
        clienteEntity.setCep(clienteRequest.cep());

        clienteEntity = clienteRepository.save(clienteEntity);
        return clienteEntity;
    }

    @Transactional
    public ClienteEntity atualizarParcial(Long id, ClienteRequest clienteRequest) {
        ClienteEntity clienteEntity = this.buscarPorId(id);

        if (clienteRequest.nome() != null) clienteEntity.setNome(clienteRequest.nome());
        if (clienteRequest.cpf() != null) clienteEntity.setCpf(clienteRequest.cpf());
        if (clienteRequest.telefone() != null) clienteEntity.setTelefone(clienteRequest.telefone());
        if (clienteRequest.cep() != null) clienteEntity.setCep(clienteRequest.cep());

        clienteEntity = clienteRepository.save(clienteEntity);
        return clienteEntity;
    }

    @Transactional
    public void deletar(Long id) {
        ClienteEntity clienteEntity = this.buscarPorId(id);
        clienteRepository.delete(clienteEntity);
    }

}
