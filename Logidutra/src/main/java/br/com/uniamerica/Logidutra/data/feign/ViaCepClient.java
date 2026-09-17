package br.com.uniamerica.Logidutra.data.feign;

import br.com.uniamerica.Logidutra.controller.dto.ViaCepResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viaCepClient", url = "https://viacep.com.br/ws")
public interface ViaCepClient {

    @GetMapping("{cep}/json/")
    ViaCepResponse buscarPorCep(@PathVariable("cep") String cep);

}