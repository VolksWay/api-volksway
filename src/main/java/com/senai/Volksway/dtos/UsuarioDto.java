package com.senai.Volksway.dtos;

import com.senai.Volksway.models.TipoModel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public record UsuarioDto(
        UUID id_empresa,
        @NotBlank String nome,

        @NotBlank @Email(message = "O email deve estar no formato válido") String email,

        @NotBlank String senha,

        @NotBlank String telefone,

        @NotBlank LocalDate data_nascimento,
        @NotBlank String cidade,
        @NotBlank String cpf,

        TipoModel tipo_usuario,
        MultipartFile img) {

}

