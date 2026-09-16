package br.com.uniamerica.Logidutra.service;

import br.com.uniamerica.Logidutra.controller.dto.ClienteRequest;
import br.com.uniamerica.Logidutra.controller.dto.ClienteResponse;
import br.com.uniamerica.Logidutra.entity.ClienteEntity;
import br.com.uniamerica.Logidutra.entity.Usuario;
import br.com.uniamerica.Logidutra.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public ClienteEntity salvar(ClienteRequest clienteRequest) {
        ClienteEntity clienteEntity = new ClienteEntity();

        clienteEntity.setNome(clienteRequest.nome());
        clienteEntity.setCpf(clienteRequest.cpf());
        clienteEntity.setTelefone(clienteRequest.telefone());
        clienteEntity.setCep(clienteRequest.cep());

        return clienteRepository.save(clienteEntity);
    }

    public List<ClienteEntity> listar(){
        return this.clienteRepository.findAll();
    }
    public ClienteEntity buscarPorId(Long id) {
        ClienteEntity clienteEntity = clienteRepository.findById(id).orElseThrow();
        return clienteEntity;
    }

    public ClienteEntity atualizar(Long id, ClienteRequest clienteRequest){
        ClienteEntity clienteEntity = this.buscarPorId(id);

        clienteEntity.setNome(clienteRequest.nome());
        clienteEntity.setCpf(clienteRequest.cpf());
        clienteEntity.setTelefone(clienteRequest.telefone());
        clienteEntity.setCep(clienteRequest.cep());
        clienteEntity.setLogradouro(clienteRequest.logradouro());
        clienteEntity.setBairro(clienteRequest.bairro());
        clienteEntity.setCidade(clienteRequest.cidade());

        return clienteRepository.save(clienteEntity);
    }

}
