package com.senai.Volksway.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.io.Serial;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "tb_usuario")
public class UsuarioModel implements Serializable, UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_usuario", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_empresa", referencedColumnName = "id_empresa")
    private EmpresaModel empresa;

    private String nome;
    private String telefone;
    private String email;
    @JsonIgnore
    private String senha;
    private LocalDate data_nascimento;
    private String cidade;
    private String cpf;
    private TipoModel tipo_usuario;
    private String img;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.tipo_usuario == TipoModel.ADMIN) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_MOTORISTA"),
                    new SimpleGrantedAuthority("ROLE_PROPRIETARIO"),
                    new SimpleGrantedAuthority("ROLE_ADMFROTA")
            );
        }else if (this.tipo_usuario == TipoModel.MOTORISTA) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_MOTORISTA")
            );

        }else if (this.tipo_usuario == TipoModel.PROPRIETARIO) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_PROPRIETARIO")
            );
        }else if (this.tipo_usuario == TipoModel.ADM_FROTA) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMFROTA")
            );
        }
        return null;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}