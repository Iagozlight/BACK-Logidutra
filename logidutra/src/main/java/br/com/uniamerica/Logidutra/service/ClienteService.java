package br.com.uniamerica.Logidutra.service;

import br.com.uniamerica.Logidutra.controller.dto.ClienteRequest;
import br.com.uniamerica.Logidutra.controller.dto.ViaCepResponse;
import br.com.uniamerica.Logidutra.data.feign.ViaCepClient;
import br.com.uniamerica.Logidutra.entity.ClienteEntity;
import br.com.uniamerica.Logidutra.entity.Usuario;
import br.com.uniamerica.Logidutra.enums.Role;
import br.com.uniamerica.Logidutra.repository.ClienteRepository;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    private final ViaCepClient viaCepClient;
    private final RoleService roleService;


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
    public ClienteEntity salvar(ClienteRequest clienteRequest, Usuario usuarioLogado) {
        roleService.validarRole(usuarioLogado.getId(), Role.ADMIN);
        log.info("Salvando cliente: {}", clienteRequest.nome());

        ViaCepResponse viaCepResponse = consultarCep(clienteRequest.cep());

        ClienteEntity clienteEntity = new ClienteEntity();

        clienteEntity.setNome(clienteRequest.nome());
        clienteEntity.setCpf(clienteRequest.cpf());
        clienteEntity.setTelefone(clienteRequest.telefone());
        clienteEntity.setCep(clienteRequest.cep());
        clienteEntity.setLogradouro(viaCepResponse.logradouro());
        clienteEntity.setBairro(viaCepResponse.bairro());
        clienteEntity.setCidade(viaCepResponse.cidade());

        ClienteEntity salvo = clienteRepository.save(clienteEntity);
        log.info("Cliente {} salvo com id {}", salvo.getNome(), salvo.getId());
        return salvo;
    }

    public List<ClienteEntity> listar() {
        return this.clienteRepository.findAll();
    }

    public ClienteEntity buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente " + id + " não encontrado"));
    }

    @Transactional
    public ClienteEntity atualizar(Long id, ClienteRequest clienteRequest, Usuario usuarioLogado) {
        roleService.validarRole(usuarioLogado.getId(), Role.ADMIN);
        log.info("Atualizando cliente {}", id);

        ClienteEntity clienteEntity = this.buscarPorId(id);
        ViaCepResponse viaCepResponse = consultarCep(clienteRequest.cep());

        clienteEntity.setNome(clienteRequest.nome());
        clienteEntity.setCpf(clienteRequest.cpf());
        clienteEntity.setTelefone(clienteRequest.telefone());
        clienteEntity.setCep(clienteRequest.cep());
        clienteEntity.setLogradouro(viaCepResponse.logradouro());
        clienteEntity.setBairro(viaCepResponse.bairro());
        clienteEntity.setCidade(viaCepResponse.cidade());

        return clienteRepository.save(clienteEntity);
    }

    @Transactional
    public ClienteEntity atualizarParcial(Long id, ClienteRequest clienteRequest, Usuario usuarioLogado) {
        roleService.validarRole(usuarioLogado.getId(), Role.ADMIN);
        log.info("Atualizando parcialmente cliente {}", id);

        ClienteEntity clienteEntity = this.buscarPorId(id);

        if (clienteRequest.nome() != null) clienteEntity.setNome(clienteRequest.nome());
        if (clienteRequest.cpf() != null) clienteEntity.setCpf(clienteRequest.cpf());
        if (clienteRequest.telefone() != null) clienteEntity.setTelefone(clienteRequest.telefone());
        if (clienteRequest.cep() != null && !clienteRequest.cep().equals(clienteEntity.getCep())) {
            ViaCepResponse endereco = consultarCep(clienteRequest.cep());
            clienteEntity.setCep(clienteRequest.cep());
            clienteEntity.setLogradouro(endereco.logradouro());
            clienteEntity.setBairro(endereco.bairro());
            clienteEntity.setCidade(endereco.cidade());
        }

        return clienteRepository.save(clienteEntity);
    }

    @Transactional
    public void deletar(Long id, Usuario usuarioLogado) {
        roleService.validarRole(usuarioLogado.getId(), Role.ADMIN);
        log.info("Deletando cliente {}", id);
        ClienteEntity clienteEntity = this.buscarPorId(id);
        clienteRepository.delete(clienteEntity);
    }

}
