package com.catalisa.taxes_api.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.catalisa.taxes_api.model.entity.User;
import com.catalisa.taxes_api.repository.UserRepository;
import com.catalisa.taxes_api.security.model.UserDetailsImpl;

public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Não foi encontrado nenhum usuário com o nome: " + username));
        return new UserDetailsImpl(user);
    }
}
