package com.catalisa.taxes_api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catalisa.taxes_api.dtos.UserRegisterDto;
import com.catalisa.taxes_api.services.UserService;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/user/register")
public class UserController {
    
    @Autowired
    private UserService userService;

    @PostMapping
    public void registerUser(@Validated @RequestBody UserRegisterDto userRegisterDto) {
        userService.registerUser(userRegisterDto);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        if ("Já existe um usuário com este nome".equals(ex.getMessage())) {
            return new ResponseEntity<>("Já existe um usuário com este nome.", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>("Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
