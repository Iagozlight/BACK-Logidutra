package br.com.uniamerica.Logidutra.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

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