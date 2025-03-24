package com.catalisa.taxes_api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catalisa.taxes_api.model.Tax;

public interface TaxRepository extends JpaRepository<Tax, Long> {
    Optional<Tax> findById(Long id);
    Optional<Tax> findByNome(String nome);

    boolean existsByNome(String nome);
}
