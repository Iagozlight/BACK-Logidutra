package br.com.uniamerica.Logidutra.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record RomaneiosRequest(
        Long id,
        @NotNull(message = "Data obrigatoria")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate data,

        @NotNull(message = "Veiculo obrigatorio")
        Long veiculoId,

        @NotNull(message = "Motorista obrigatorio")
        Long usuarioId,

        @NotEmpty(message = "Informe ao menos um cliente")
        List<Long> clienteId,

        List<Long> produtoId
) {
}
