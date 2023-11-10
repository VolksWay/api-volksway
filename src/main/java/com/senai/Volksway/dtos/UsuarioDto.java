package com.senai.Volksway.dtos;

import com.senai.Volksway.models.TipoModel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import java.util.UUID;

public record UsuarioDto(
        @NotBlank UUID id_empresa,
        @NotBlank String nome,

        @NotBlank @Email(message = "O email deve estar no formato válido") String email,

        @NotBlank String senha,

        @NotBlank String telefone,

        @NotBlank Date data_nascimento,
        @NotBlank String cidade,
        @NotBlank String cpf,

        TipoModel tipo_usuario) {

}

