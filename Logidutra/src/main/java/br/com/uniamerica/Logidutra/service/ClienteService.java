package br.com.uniamerica.Logidutra.service;

import br.com.uniamerica.Logidutra.controller.dto.ClienteRequest;
import br.com.uniamerica.Logidutra.controller.dto.ClienteResponse;
import br.com.uniamerica.Logidutra.entity.ClienteEntity;
import br.com.uniamerica.Logidutra.repository.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService (ClienteRepository clienteRepository) { this.clienteRepository = clienteRepository; }

    public List<ClienteEntity> findAll() { return clienteRepository.findAll(); }

    public ClienteEntity findById(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Carro não encontrado com o id: " + id));
    }

    public ClienteEntity salvar(ClienteEntity clienteEntity) { return clienteRepository.save(clienteEntity); }

    public ClienteEntity atualizar(Long id, ClienteEntity clienteEntity) {
        ClienteEntity clienteExistente = findById(id);

        clienteExistente.setNome(clienteEntity.getNome());
        clienteExistente.setCpf(clienteEntity.getCpf());
        clienteExistente.setTelefone(clienteEntity.getTelefone());
        clienteExistente.setCep(clienteEntity.getCep());

        return clienteRepository.save(clienteExistente);
    }

    public void delete(Long id) {
        ClienteEntity cliente = findById(id);

        clienteRepository.delete(cliente);
    }

}
