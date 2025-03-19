package com.catalisa.taxes_api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catalisa.taxes_api.dtos.AuthResponseDto;
import com.catalisa.taxes_api.dtos.LoginDto;
import com.catalisa.taxes_api.services.AuthenticationService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários")
@AllArgsConstructor
@RestController
@RequestMapping("/user/login")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authService;

    @PostMapping
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto){

        String token = authService.login(loginDto);

        AuthResponseDto authResponseDto = new AuthResponseDto();
        authResponseDto.setAccessToken(token);

        return new ResponseEntity<>(authResponseDto, HttpStatus.OK);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        if ("Invalid credentials".equals(ex.getMessage())) {
            return new ResponseEntity<>("Verifique usuário e senha.", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>("Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}