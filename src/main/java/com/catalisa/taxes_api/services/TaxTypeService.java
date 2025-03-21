package com.catalisa.taxes_api.services;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    private static final Set<String> ALLOWED_FIELDS = Set.of("nome", "descricao", "aliquota");

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

    public Tax updateTaxById(Long id, Map<String, Object> taxTypeUpdateDto) {
        Tax taxToBeUpdated = taxRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Tipo de imposto com ID " + id + " não encontrado."));


            taxTypeUpdateDto.forEach((key, value) -> {
                if (ALLOWED_FIELDS.contains(key)) {
                    try {
                        var field = Tax.class.getDeclaredField(key);
                        field.setAccessible(true);
                        field.set(taxToBeUpdated, value);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException("Campo inválido: " + key, e);
                    }
                } else {
                    throw new RuntimeException("Atualização de campo não permitida: " + key);
                }
            });

        taxRepository.save(taxToBeUpdated);
        
        return taxToBeUpdated;
    }

    public Tax deleteTaxTypeById(Long id) {
        Tax taxToBeDeleted = taxRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Tipo de imposto com ID " + id + " não encontrado."));

        taxRepository.delete(taxToBeDeleted);
    
        return taxToBeDeleted;
    }

}
