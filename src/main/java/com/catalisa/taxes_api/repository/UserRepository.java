package com.catalisa.taxes_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catalisa.taxes_api.model.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
