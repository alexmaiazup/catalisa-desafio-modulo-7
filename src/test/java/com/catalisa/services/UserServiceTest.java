package com.catalisa.services;

import com.catalisa.taxes_api.dtos.RoleUser;
import com.catalisa.taxes_api.dtos.UserRegisterDto;
import com.catalisa.taxes_api.model.Role;
import com.catalisa.taxes_api.model.User;
import com.catalisa.taxes_api.repositories.RoleRepository;
import com.catalisa.taxes_api.repositories.UserRepository;
import com.catalisa.taxes_api.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRegisterUserSuccessfully() {

        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setUsername("testuser");
        userRegisterDto.setPassword("password123");

        Set<Role> roles = new HashSet<>();

        Role role = new Role();
        role.setRole(RoleUser.ROLE_ADMIN);
        roles.add(role);
        userRegisterDto.setRoles(roles);

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(bCryptPasswordEncoder.encode("password123")).thenReturn("encodedPassword");


        userService.registerUser(userRegisterDto);


        verify(userRepository, times(1)).existsByUsername("testuser");
        verify(bCryptPasswordEncoder, times(1)).encode("password123");
        verify(roleRepository, times(1)).saveAll(anySet());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {

        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setUsername("existinguser");
        userRegisterDto.setPassword("password123");

        when(userRepository.existsByUsername("existinguser")).thenReturn(true);


        assertThrows(RuntimeException.class, () -> userService.registerUser(userRegisterDto));

        verify(userRepository, times(1)).existsByUsername("existinguser");
        verifyNoInteractions(bCryptPasswordEncoder);
        verifyNoInteractions(roleRepository);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldSaveRolesCorrectly() {

        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setUsername("testuser");
        userRegisterDto.setPassword("password123");

        Set<Role> roles = new HashSet<>();

        Role role1 = new Role();
        Role role2 = new Role();

        role1.setRole(RoleUser.ROLE_DEFAULT);
        role2.setRole(RoleUser.ROLE_ADMIN);

        roles.add(role1);
        roles.add(role2);

        userRegisterDto.setRoles(roles);

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(bCryptPasswordEncoder.encode("password123")).thenReturn("encodedPassword");

        userService.registerUser(userRegisterDto);

        verify(roleRepository, times(1)).saveAll(anySet());
        verify(userRepository, times(1)).save(any(User.class));
    }
}