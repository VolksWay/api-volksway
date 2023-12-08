package com.senai.Volksway.controllers;

import com.senai.Volksway.dtos.LoginDto;
import com.senai.Volksway.dtos.TokenDto;
import com.senai.Volksway.models.UsuarioModel;
import com.senai.Volksway.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class LoginController {
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginDto dadosLogin){
        var userNamePassword  = new UsernamePasswordAuthenticationToken(dadosLogin.email(),dadosLogin.senha());

        var auth = authenticationManager.authenticate(userNamePassword);

        var token = tokenService.gerarToken((UsuarioModel) auth.getPrincipal());

        return ResponseEntity.status(HttpStatus.OK).body(new TokenDto(token));
    }
}