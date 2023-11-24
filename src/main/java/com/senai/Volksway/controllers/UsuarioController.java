package com.senai.Volksway.controllers;

import com.senai.Volksway.dtos.EmpresaDto;
import com.senai.Volksway.dtos.UsuarioDto;
import com.senai.Volksway.models.EmpresaModel;
import com.senai.Volksway.models.UsuarioModel;
import com.senai.Volksway.repositories.EmpresaRepository;
import com.senai.Volksway.repositories.UsuarioRepository;
import com.senai.Volksway.services.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.senai.Volksway.controllers.EmpresaController;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController //Annotation para definir controller
@RequestMapping(value = "/usuarios", produces = {"application/json"})
public class UsuarioController {
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    EmpresaRepository empresaRepository;
    @Autowired
    FileUploadService fileUploadServices;

    @GetMapping
    public ResponseEntity<List<UsuarioModel>> listarUsuarios() {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioRepository.findAll());
    }

    ;

    @GetMapping("/{idUsuario}")
    public ResponseEntity<Object> buscarUsuario(@PathVariable(value = "idUsuario") UUID id) {
        Optional<UsuarioModel> usuarioBuscado = usuarioRepository.findById(id);

        if (usuarioBuscado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(usuarioBuscado.get());
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Método para cadastrar um usuario", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cadastro foi efetuado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
    })
    public ResponseEntity<Object> criarUsuario(@ModelAttribute @Valid UsuarioDto usuarioDto) {
        UsuarioModel usuarioModel = new UsuarioModel();

        // Copie as propriedades do UsuarioDto para UsuarioModel
        BeanUtils.copyProperties(usuarioDto, usuarioModel);

        // Busque a empresa pelo CNPJ
        EmpresaModel empresa = empresaRepository.findByCnpj(usuarioDto.cnpj_empresa());

        if (empresa != null) {
            usuarioModel.setEmpresa(empresa);
        } else {
            // Se a empresa não for encontrada, crie uma nova usando o DTO
            EmpresaDto empresaDto = new EmpresaDto(
                    usuarioDto.razao_social(),
                    usuarioDto.cidade_empresa(),
                    usuarioDto.cnpj_empresa()
            );

            EmpresaModel novaEmpresa = new EmpresaModel();
            BeanUtils.copyProperties(empresaDto, novaEmpresa);
            empresaRepository.save(novaEmpresa);

            usuarioModel.setEmpresa(novaEmpresa);
        }

        String urlImagem;

        try {
            urlImagem = fileUploadServices.fazerUpload(usuarioDto.img());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        usuarioModel.setImg(urlImagem);

        String senhaCript = new BCryptPasswordEncoder().encode(usuarioDto.senha());
        usuarioModel.setSenha(senhaCript);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRepository.save(usuarioModel));
    }

    @PutMapping("/{idUsuario}") //PathVariable = Pega o valor passado da url, RequestBody: Pega os valores do Body
    public ResponseEntity<Object> alterarUsuario(@PathVariable(value = "idUsuario") UUID id, @RequestBody @Valid UsuarioDto usuarioDto) {
        Optional<UsuarioModel> usuarioBuscado = usuarioRepository.findById(id); //Procura o usuario atraves do id, caso nao existe ele nao prossegue

        if (usuarioBuscado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }

        UsuarioModel usuarioDb = usuarioBuscado.get();

        BeanUtils.copyProperties(usuarioDto, usuarioDb); // as informações do dto são puxadas pela url e passadas para o usuario do database

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(usuarioRepository.save(usuarioDb));
    }

    ;

    @PatchMapping("/{idUsuario}")
    public ResponseEntity<Object> atualizarEmailUsuario(@PathVariable(value = "idUsuario") UUID id, @RequestBody UsuarioDto usuarioDto) {
        Optional<UsuarioModel> usuarioBuscado = usuarioRepository.findById(id);

        if (usuarioBuscado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }

        UsuarioModel usuarioDb = usuarioBuscado.get();

        if (usuarioDto.email() != null && !usuarioDto.email().isEmpty()) {
            usuarioDb.setEmail(usuarioDto.email());
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(usuarioRepository.save(usuarioDb));
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Object> deletarUsuario(@PathVariable(value = "idUsuario") UUID id) {
        Optional<UsuarioModel> usuarioBuscado = usuarioRepository.findById(id);

        if (usuarioBuscado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }

        usuarioRepository.delete(usuarioBuscado.get()); //deletar o usuario buscasdo

        return ResponseEntity.status(HttpStatus.OK).body("Usuário deletado com sucesso!");
    }

    ;
}
