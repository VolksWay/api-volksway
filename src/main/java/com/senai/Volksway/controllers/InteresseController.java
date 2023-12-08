package com.senai.Volksway.controllers;

import com.senai.Volksway.dtos.InteresseDto;
import com.senai.Volksway.models.InteresseModel;
import com.senai.Volksway.repositories.InteresseRepository;
import com.senai.Volksway.repositories.PropagandaRepository;
import com.senai.Volksway.repositories.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController //Annotation para definir controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/interesses", produces = {"application/json"})
public class InteresseController {
    @Autowired
    InteresseRepository interesseRepository;

    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    PropagandaRepository propagandaRepository;

    @GetMapping("/{idInteresse}")
    public ResponseEntity<Object> buscarInteresse(@PathVariable(value = "idInteresse") UUID id){
        Optional<InteresseModel> interesseBuscado = interesseRepository.findById(id);

        if (interesseBuscado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Interesse não encontrado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(interesseBuscado.get());
    }

    @GetMapping
    public ResponseEntity<List<InteresseModel>> listarInteresses() {
        return ResponseEntity.status(HttpStatus.OK).body(interesseRepository.findAll());
    };

    @PostMapping
    public ResponseEntity<Object> criarInteresse(@RequestBody @Valid InteresseDto interesseDto){
        InteresseModel novoInteresse = new InteresseModel();
        BeanUtils.copyProperties(interesseDto, novoInteresse);

        var propaganda = propagandaRepository.findById(interesseDto.id_propaganda());

        if (propaganda.isPresent()) {
            novoInteresse.setPropaganda(propaganda.get());
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id propaganda não encontrado");
        }

        var usuario = usuarioRepository.findById(interesseDto.id_usuario());

        if (usuario.isPresent()) {
            novoInteresse.setUsuario(usuario.get());
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id usuario não encontrado");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(interesseRepository.save(novoInteresse));
    }
}
