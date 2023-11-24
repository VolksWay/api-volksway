package com.senai.Volksway.dtos;

import com.senai.Volksway.models.UsuarioModel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

public record PropagandaDto(
        UUID id_usuario,
        @NotBlank String titulo,

        @NotBlank String url,

        @NotBlank String descricao,
        @NotBlank String publico_alvo,

       @DateTimeFormat(pattern = "yyyy-MM-dd") Date data_limite,

         float preco,
         boolean importancia,
        @NotBlank String nomeTipoPropaganda,

        MultipartFile img) {

}