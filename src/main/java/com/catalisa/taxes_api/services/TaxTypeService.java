package com.catalisa.taxes_api.services;

import org.springframework.stereotype.Service;

import javax.naming.NameAlreadyBoundException;

import org.springframework.beans.factory.annotation.Autowired;

import com.catalisa.taxes_api.dtos.TaxRegisterDto;
import com.catalisa.taxes_api.model.Tax;
import com.catalisa.taxes_api.repositories.TaxRepository;

@Service
public class TaxTypeService {

    @Autowired
    private TaxRepository taxRepository;

    public void registerTaxType(TaxRegisterDto taxRegisterDto) throws NameAlreadyBoundException {
        if (taxRepository.existsByNome(taxRegisterDto.getNome())) {
            throw new NameAlreadyBoundException("Já existe um tipo de imposto com esse nome.");
        }

        Tax newTax = new Tax();
        newTax.setNome(taxRegisterDto.getNome());
        newTax.setDescricao(taxRegisterDto.getDescricao());
        newTax.setAliquota(taxRegisterDto.getAliquota());

        taxRepository.save(newTax);
    }


}
