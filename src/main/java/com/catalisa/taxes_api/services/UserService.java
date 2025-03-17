package com.catalisa.taxes_api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.catalisa.taxes_api.dtos.UserRegisterDto;
import com.catalisa.taxes_api.model.Role;
import com.catalisa.taxes_api.model.User;
import com.catalisa.taxes_api.repositories.RoleRepository;
import com.catalisa.taxes_api.repositories.UserRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void registerUser(UserRegisterDto registerUserDto){
        if (userRepository.existsByUsername(registerUserDto.getUsername())){
            throw new RuntimeException("Já existe um usuário com este nome.");
        }

        User user = new User();
        user.setUsername(registerUserDto.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(registerUserDto.getPassword()));

        Set<Role> roles = registerUserDto.getRoles()
            .stream()
            .map(r -> new Role(r.getRole().name()))
            .collect(Collectors.toSet());
        roleRepository.saveAll(roles);

        user.setRoles(roles);
        userRepository.save(user);

    }
}
