package com.catalisa.taxes_api.services;

import org.springframework.stereotype.Service;

import java.util.List;

import javax.naming.NameAlreadyBoundException;

import org.springframework.beans.factory.annotation.Autowired;

import com.catalisa.taxes_api.dtos.TaxRegisterDto;
import com.catalisa.taxes_api.model.Tax;
import com.catalisa.taxes_api.repositories.TaxRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TaxTypeService {

    @Autowired
    private TaxRepository taxRepository;

    public Tax registerTaxType(TaxRegisterDto taxRegisterDto) throws NameAlreadyBoundException {
        if (taxRepository.existsByNome(taxRegisterDto.getNome())) {
            throw new NameAlreadyBoundException("Já existe um tipo de imposto com esse nome.");
        }

        Tax newTax = new Tax();
        newTax.setNome(taxRegisterDto.getNome());
        newTax.setDescricao(taxRegisterDto.getDescricao());
        newTax.setAliquota(taxRegisterDto.getAliquota());

        taxRepository.save(newTax);

        return newTax;
    }

    public List<Tax> listAllTaxTypes() {
        return taxRepository.findAll();
    }

    public Tax getTaxById(Long id) {
        return taxRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Tipo de imposto com ID " + id + " não encontrado."));
    }

}
