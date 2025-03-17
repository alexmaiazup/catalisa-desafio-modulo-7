package com.catalisa.taxes_api.dtos;

import lombok.Data;

import java.util.Set;

import com.catalisa.taxes_api.model.Role;

@Data
public class UserRegisterDto {
    private String username;
    private String password;
    private Set<Role> roles;
}