package com.senai.Volksway.repositories;

import com.senai.Volksway.models.EmpresaModel;
import com.senai.Volksway.models.InteresseModel;
import com.senai.Volksway.models.UsuarioModel;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

@Repository //annotation do repository
public interface InteresseRepository extends JpaRepository<InteresseModel, UUID> {
    List<InteresseModel> findByUsuario(UsuarioModel usuarioModel);
}

