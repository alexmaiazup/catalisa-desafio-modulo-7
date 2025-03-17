package com.catalisa.taxes_api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catalisa.taxes_api.dtos.UserRegisterDto;
import com.catalisa.taxes_api.services.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/user/register")
public class UserController {
    
    @Autowired
    private UserService userService;

    @PostMapping
    public void registerUser(@RequestBody UserRegisterDto userRegisterDto) {
        userService.registerUser(userRegisterDto);
    }
    
}
