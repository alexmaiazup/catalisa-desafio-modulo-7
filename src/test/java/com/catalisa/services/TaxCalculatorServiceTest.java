package com.catalisa.services;

import com.catalisa.taxes_api.dtos.CalculateTaxesDto;
import com.catalisa.taxes_api.model.Tax;
import com.catalisa.taxes_api.repositories.TaxRepository;
import com.catalisa.taxes_api.services.TaxCalculatorService;
import com.catalisa.taxes_api.utils.TaxFormula;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaxCalculatorServiceTest {

    @InjectMocks
    private TaxCalculatorService taxCalculatorService;

    @Mock
    private TaxRepository taxRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCalculateTax() {
        
        Long tipoImpostoId = 1L;
        double valorBase = 1000.0;
        CalculateTaxesDto calculateTaxesDto = new CalculateTaxesDto(tipoImpostoId, valorBase);

        Tax tax = new Tax();
        tax.setId(tipoImpostoId);
        tax.setNome("ICMS");
        tax.setAliquota(18.0);

        when(taxRepository.findById(tipoImpostoId)).thenReturn(Optional.of(tax));

        
        TaxFormula result = taxCalculatorService.calculateTax(calculateTaxesDto);

        
        assertNotNull(result);
        assertEquals("ICMS", result.getTipoImposto());
        assertEquals(1000.0, result.getValorBase());
        assertEquals(18.0, result.getAliquota());
        assertEquals(180.0, result.getValorImposto());

        verify(taxRepository, times(1)).findById(tipoImpostoId);
    }

    @Test
    void shouldThrowExceptionWhenTaxTypeNotFound() {
        Long tipoImpostoId = 1L;
        double valorBase = 1000.0;
        CalculateTaxesDto calculateTaxesDto = new CalculateTaxesDto(tipoImpostoId, valorBase);

        when(taxRepository.findById(tipoImpostoId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            taxCalculatorService.calculateTax(calculateTaxesDto);
        });

        assertEquals("Tipo de imposto não encontrado.", exception.getMessage());
        verify(taxRepository, times(1)).findById(tipoImpostoId);
    }

    @Test
    void shouldThrowExceptionWhenValorBaseIsNegative() {
        
        Long tipoImpostoId = 1L;
        double valorBase = -1000.0;
        CalculateTaxesDto calculateTaxesDto = new CalculateTaxesDto(tipoImpostoId, valorBase);
        Tax tax = new Tax();
        tax.setId(tipoImpostoId);
        tax.setNome("ICMS");
        tax.setAliquota(18.0);
        when(taxRepository.findById(tipoImpostoId)).thenReturn(Optional.of(tax));

        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            taxCalculatorService.calculateTax(calculateTaxesDto);
        });
        assertEquals("O valor base não pode ser negativo.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenTipoImpostoIdIsNull() {
        
        Long tipoImpostoId = null;
        double valorBase = 1000.0;
        CalculateTaxesDto calculateTaxesDto = new CalculateTaxesDto(tipoImpostoId, valorBase);

        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            taxCalculatorService.calculateTax(calculateTaxesDto);
        });
        assertEquals("O ID do tipo de imposto não pode ser nulo.", exception.getMessage());
        verify(taxRepository, never()).findById(any());
    }

    @Test
    void shouldThrowExceptionWhenRepositoryThrowsException() {
        
        Long tipoImpostoId = 1L;
        double valorBase = 1000.0;
        CalculateTaxesDto calculateTaxesDto = new CalculateTaxesDto(tipoImpostoId, valorBase);
        when(taxRepository.findById(tipoImpostoId)).thenThrow(new RuntimeException("Erro inesperado no repositório."));

        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            taxCalculatorService.calculateTax(calculateTaxesDto);
        });
        assertEquals("Erro inesperado no repositório.", exception.getMessage());
        verify(taxRepository, times(1)).findById(tipoImpostoId);
    }
}
