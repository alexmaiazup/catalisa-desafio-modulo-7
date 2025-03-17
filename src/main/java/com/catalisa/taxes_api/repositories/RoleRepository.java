package com.catalisa.taxes_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catalisa.taxes_api.model.Role;

public interface RoleRepository extends JpaRepository <Role, Long> {

}
