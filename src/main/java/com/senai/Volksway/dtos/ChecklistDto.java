package com.senai.Volksway.dtos;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record ChecklistDto(
        @NotBlank UUID id_veiculo,
        @NotBlank boolean freio,

        @NotBlank boolean combustivel,
        @NotBlank boolean oleo,
        @NotBlank boolean ar_condicionado,
        @NotBlank String foto_pneu) {

}