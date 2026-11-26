package br.com.uniamerica.logidutra.service;

import br.com.uniamerica.logidutra.controller.dto.ClienteRequest;
import br.com.uniamerica.logidutra.controller.dto.ViaCepResponse;
import br.com.uniamerica.logidutra.data.feign.ViaCepClient;
import br.com.uniamerica.logidutra.entity.ClienteEntity;
import br.com.uniamerica.logidutra.entity.Usuario;
import br.com.uniamerica.logidutra.enums.Role;
import br.com.uniamerica.logidutra.exception.ApiExternaIndisponivelException;
import br.com.uniamerica.logidutra.exception.CepInvalidoException;
import br.com.uniamerica.logidutra.exception.CepNaoEncontradoException;
import br.com.uniamerica.logidutra.exception.RegistroNaoEncontradoException;
import br.com.uniamerica.logidutra.repository.ClienteRepository;
import feign.FeignException;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ViaCepClient viaCepClient;


    private ViaCepResponse consultarCep(String cep) {
        if (cep == null || cep.isBlank()){
            throw new IllegalArgumentException("Cep não pode ser nulo ou String vazia!");
        }

        log.info("Consultando CEP {} na ViaCEP", cep);

        ViaCepResponse viaCepResponse;
        try{
            viaCepResponse = viaCepClient.buscarPorCep(cep);
        }catch(FeignException e){
            int status = e.status();
            if(status == 500 || status == 501 || status == 502 || status == 503 || status == 504){
                log.error("Erro de comunicação com o ViaCep: {}", e.getMessage());
                throw new ApiExternaIndisponivelException("Erro de comunicação com o ViaCep: " + e.getMessage());
            }else if(status == 400){
                throw new CepInvalidoException("Formato de CEP invalido: " + e.getMessage());
            }
            else{
                throw new RuntimeException("ERRO: " + e.getMessage());
            }
            
        }

        if (viaCepResponse == null || Boolean.TRUE.equals(viaCepResponse.erro())) {
            log.warn("CEP {} não encontrado na ViaCEP", cep);
            throw new CepNaoEncontradoException("CEP não encontrado:" + cep);
        }
        return viaCepResponse;
    }

    @Transactional
    public ClienteEntity salvar(ClienteRequest clienteRequest, Usuario usuarioLogado) {
        log.info("Usuário {} salvando cliente: {}", usuarioLogado.getNome(), clienteRequest.nome());

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
        return clienteRepository.findById(id).orElseThrow(() -> new RegistroNaoEncontradoException("Não foi possivel encontrar cliente com ID: " + id));
    }

    @Transactional
    public ClienteEntity atualizar(Long id, ClienteRequest clienteRequest, Usuario usuarioLogado) {
        log.info("Usuário {} atualizando cliente {}", usuarioLogado.getNome(), id);

        ClienteEntity clienteEntity = buscarPorId(id);
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
        log.info("Usuário {} atualizando parcialmente cliente {}", usuarioLogado.getNome(), id);

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
        log.info("Usuário {} deletando cliente {}", usuarioLogado.getNome(), id);
        ClienteEntity clienteEntity = this.buscarPorId(id);
        clienteRepository.delete(clienteEntity);
    }

}
