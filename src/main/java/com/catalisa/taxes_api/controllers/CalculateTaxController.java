package com.catalisa.taxes_api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catalisa.taxes_api.dtos.CalculateTaxesDto;
import com.catalisa.taxes_api.services.TaxCalculatorService;
import com.catalisa.taxes_api.utils.TaxFormula;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Tag(name = "Cálculo de impostos", description = "Endpoints para calcular valores com base em alíquotas.")
@AllArgsConstructor
@RestController
@RequestMapping("/calculo")
public class CalculateTaxController {

    @Autowired
    private TaxCalculatorService taxCalculationService;

    @PostMapping
    public TaxFormula calculateTax(@RequestBody CalculateTaxesDto calculateTaxesDto) {
        return taxCalculationService.calculateTax(calculateTaxesDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
    
}
