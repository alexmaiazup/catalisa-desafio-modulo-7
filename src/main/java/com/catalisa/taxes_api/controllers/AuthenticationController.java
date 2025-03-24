package com.catalisa.taxes_api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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

@Tag(name = "Login", description = "Endpoints para autenticação de usuários")
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

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}