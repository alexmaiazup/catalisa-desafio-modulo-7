package com.catalisa.taxes_api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catalisa.taxes_api.dtos.CalculateTaxesDto;
import com.catalisa.taxes_api.model.Tax;
import com.catalisa.taxes_api.repositories.TaxRepository;
import com.catalisa.taxes_api.utils.TaxFormula;

@Service
public class TaxCalculatorService {

    @Autowired
    private TaxRepository taxTypeRepository;
    private TaxFormula taxFormula;

    public TaxFormula calculateTax(CalculateTaxesDto calculateTaxesDto) {

        if (calculateTaxesDto.getTipoImpostoId() == null) {
            throw new IllegalArgumentException("O ID do tipo de imposto não pode ser nulo.");
        }

        if (calculateTaxesDto.getValorBase() < 0) {
            throw new IllegalArgumentException("O valor base não pode ser negativo.");
        }

        Tax taxType = taxTypeRepository.findById(calculateTaxesDto.getTipoImpostoId())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de imposto não encontrado."));

        double valorImposto = calculateTaxesDto.getValorBase() * (taxType.getAliquota() / 100);

        taxFormula = new TaxFormula(taxType.getNome(), calculateTaxesDto.getValorBase(), taxType.getAliquota(), valorImposto);

        return taxFormula;
    }
}
