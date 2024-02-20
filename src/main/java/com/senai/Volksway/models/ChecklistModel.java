package com.senai.Volksway.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.io.Serial;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "tb_checklist")
public class ChecklistModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_checklist", nullable = false)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private UsuarioModel usuario;

    private boolean freio;

    private boolean combustivel;
    private boolean oleo;
    private boolean ar_condicionado;
    private LocalDate data_criado;
    private String foto_pneu;
    private String estado_pneu;
}
