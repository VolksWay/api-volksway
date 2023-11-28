package com.senai.Volksway.controllers;

import com.senai.Volksway.dtos.EmpresaDto;
import com.senai.Volksway.dtos.UsuarioDto;
import com.senai.Volksway.models.EmpresaModel;
import com.senai.Volksway.models.InteresseModel;
import com.senai.Volksway.models.UsuarioModel;
import com.senai.Volksway.models.VeiculoModel;
import com.senai.Volksway.repositories.EmpresaRepository;
import com.senai.Volksway.repositories.InteresseRepository;
import com.senai.Volksway.repositories.UsuarioRepository;
import com.senai.Volksway.repositories.VeiculoRepository;
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
    InteresseRepository interesseRepository;

    @Autowired
    VeiculoRepository veiculoRepository;
    @Autowired
    FileUploadService fileUploadServices;

    @GetMapping
    public ResponseEntity<List<UsuarioModel>> listarUsuarios() {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioRepository.findAll());
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<Object> buscarUsuario(@PathVariable(value = "idUsuario") UUID id) {
        Optional<UsuarioModel> usuarioBuscado = usuarioRepository.findById(id);

        if (usuarioBuscado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(usuarioBuscado.get());
    }
    @GetMapping("/{idUsuario}/interesses")
    public ResponseEntity<Object> listarInteressesPorUsuario(@PathVariable(value = "idUsuario") UUID id) {
        // Procurar o usuário pelo ID
        Optional<UsuarioModel> usuarioBuscado = usuarioRepository.findById(id);

        // Verificar se o usuário foi encontrado
        if (usuarioBuscado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }

        List<InteresseModel> interessesDoUsuario = interesseRepository.findByUsuario(usuarioBuscado.get());

        // Verificar se o usuário possui interesses
        if (interessesDoUsuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não possui interesses cadastrados");
        }

        // Retornar a lista de interesses encontrados
        return ResponseEntity.status(HttpStatus.OK).body(interessesDoUsuario);
    }

    @GetMapping("/{idUsuario}/veiculos")
    public ResponseEntity<Object> listarVeiculosPorUsuario(@PathVariable(value = "idUsuario") UUID id) {
        // Procurar o usuário pelo ID
        Optional<UsuarioModel> usuarioBuscado = usuarioRepository.findById(id);

        // Verificar se o usuário foi encontrado
        if (usuarioBuscado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }

        List<VeiculoModel> veiculosDoUsuario = veiculoRepository.findByUsuario(usuarioBuscado.get());


        // Verificar se o usuário possui veiculos
        if (veiculosDoUsuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não possui veiculo");
        }

        // Retornar a lista de veiculos encontrados
        return ResponseEntity.status(HttpStatus.OK).body(veiculosDoUsuario);
    };

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Método para cadastrar um usuario", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cadastro foi efetuado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
    })
    public ResponseEntity<Object> criarUsuario(@ModelAttribute @Valid UsuarioDto usuarioDto) {
        UsuarioModel usuarioModel = new UsuarioModel();

        BeanUtils.copyProperties(usuarioDto, usuarioModel);

        EmpresaModel empresa = empresaRepository.findByCnpj(usuarioDto.cnpj_empresa());

        if (empresa != null) {
            usuarioModel.setEmpresa(empresa);
        } else {
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
    };

    @PutMapping(value = "/{idUsuario}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> editarUsuario(@PathVariable(value = "idUsuario") UUID id, @ModelAttribute @Valid UsuarioDto usuarioDto){

        Optional<UsuarioModel> usuarioBuscado = usuarioRepository.findById(id);

        if (usuarioBuscado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }

        UsuarioModel usuarioBd = usuarioBuscado.get();
        BeanUtils.copyProperties(usuarioDto, usuarioBd);

        String urlImagem;

        try{
            urlImagem = fileUploadServices.fazerUpload(usuarioDto.img());
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        usuarioBd.setImg(urlImagem);

        return ResponseEntity.status(HttpStatus.OK).body(usuarioRepository.save(usuarioBd));
    };

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
    };

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Object> deletarUsuario(@PathVariable(value = "idUsuario") UUID id) {
        Optional<UsuarioModel> usuarioBuscado = usuarioRepository.findById(id);

        if (usuarioBuscado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }

        usuarioRepository.delete(usuarioBuscado.get()); //deletar o usuario buscasdo

        return ResponseEntity.status(HttpStatus.OK).body("Usuário deletado com sucesso!");
    };
}
