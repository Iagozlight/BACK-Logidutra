package br.com.uniamerica.Logidutra.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RomaneiosRequest(
        Long id,
        @NotNull(message = "Data obrigatoria")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate data
){
}
