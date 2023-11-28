package com.senai.Volksway.dtos;

import com.senai.Volksway.models.VeiculoModel;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

public record ChecklistDto(
        UUID id_veiculo,
        boolean freio,

        boolean combustivel,
        boolean oleo,
        boolean ar_condicionado,
        MultipartFile foto_pneu) {

}