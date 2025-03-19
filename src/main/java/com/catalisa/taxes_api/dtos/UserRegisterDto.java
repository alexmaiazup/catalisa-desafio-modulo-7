package com.catalisa.taxes_api.dtos;

import lombok.Data;

import java.util.Set;
import jakarta.validation.constraints.NotBlank;

import com.catalisa.taxes_api.model.Role;

@Data
public class UserRegisterDto {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
    private Set<Role> roles;
}