package br.com.uniamerica.logidutra.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// @JsonIgnoreProperties evita erro caso a API mande algum campo extra que a gente não mapeou
@JsonIgnoreProperties(ignoreUnknown = true)
public record ViaCepResponse (
        String cep,
        String logradouro,
        String bairro,
        @JsonProperty("localidade") String cidade,
        Boolean erro
) {
}