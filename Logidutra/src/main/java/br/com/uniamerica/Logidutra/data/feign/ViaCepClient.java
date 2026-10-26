package br.com.uniamerica.logidutra.data.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.uniamerica.logidutra.controller.dto.ViaCepResponse;

@FeignClient(name = "viaCepClient", url = "https://viacep.com.br/ws")
public interface ViaCepClient {

    @GetMapping("{cep}/json/")
    ViaCepResponse buscarPorCep(@PathVariable("cep") String cep);

}