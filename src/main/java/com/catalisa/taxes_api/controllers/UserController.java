package com.catalisa.taxes_api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catalisa.taxes_api.dtos.UserRegisterDto;
import com.catalisa.taxes_api.services.UserService;
import com.catalisa.taxes_api.utils.ResourceAlreadyExistsException;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Registro", description = "Endpoint para registrar um novo usuário")
@RestController
@RequestMapping("/user/register")
public class UserController {
    
    @Autowired
    private UserService userService;

    @PostMapping
    public void registerUser(@Validated @RequestBody UserRegisterDto userRegisterDto) {
        userService.registerUser(userRegisterDto);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<String> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
