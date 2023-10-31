package com.senai.Volksway.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.io.Serial;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "tb_propaganda")
public class PropagandaModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_propaganda", nullable = false)
    @NotBlank UUID id_usuario;
    @NotBlank String titulo;

    @NotBlank String url;

    @NotBlank String descricao;

    @NotBlank Date img;
    @NotBlank String publico_alvo;
    @NotBlank Date data_limite;

    @NotBlank float preco;
    @NotBlank boolean importancia;
    @NotBlank String nomeTipoPropaganda;
}