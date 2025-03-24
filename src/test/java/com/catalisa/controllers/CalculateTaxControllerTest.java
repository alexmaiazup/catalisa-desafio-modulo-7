package com.catalisa.controllers;

import com.catalisa.taxes_api.controllers.CalculateTaxController;
import com.catalisa.taxes_api.dtos.CalculateTaxesDto;
import com.catalisa.taxes_api.services.TaxCalculatorService;
import com.catalisa.taxes_api.utils.TaxFormula;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CalculateTaxControllerTest {

    @InjectMocks
    private CalculateTaxController calculateTaxController;

    @Mock
    private TaxCalculatorService taxCalculatorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateTax() {
        
        CalculateTaxesDto calculateTaxesDto = new CalculateTaxesDto(1L, 1000.0);
        TaxFormula expectedTaxFormula = new TaxFormula("ICMS", 1000.0, 18.0, 180.0);

        when(taxCalculatorService.calculateTax(calculateTaxesDto)).thenReturn(expectedTaxFormula);

        
        TaxFormula result = calculateTaxController.calculateTax(calculateTaxesDto);

        
        assertNotNull(result);
        assertEquals(expectedTaxFormula.getTipoImposto(), result.getTipoImposto());
        assertEquals(expectedTaxFormula.getValorBase(), result.getValorBase());
        assertEquals(expectedTaxFormula.getAliquota(), result.getAliquota());
        assertEquals(expectedTaxFormula.getValorImposto(), result.getValorImposto());

        verify(taxCalculatorService, times(1)).calculateTax(calculateTaxesDto);
    }

    @Test
    void testCalculateTax_NegativeValorBase() {
        
        CalculateTaxesDto calculateTaxesDto = new CalculateTaxesDto(1L, -1000.0);
        when(taxCalculatorService.calculateTax(calculateTaxesDto))
            .thenThrow(new IllegalArgumentException("O valor base não pode ser negativo."));

        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            calculateTaxController.calculateTax(calculateTaxesDto);
        });
        assertEquals("O valor base não pode ser negativo.", exception.getMessage());
        verify(taxCalculatorService, times(1)).calculateTax(calculateTaxesDto);
    }

    @Test
    void testCalculateTax_NullTipoImpostoId() {
        
        CalculateTaxesDto calculateTaxesDto = new CalculateTaxesDto(null, 1000.0);
        when(taxCalculatorService.calculateTax(calculateTaxesDto))
            .thenThrow(new IllegalArgumentException("O ID do tipo de imposto não pode ser nulo."));

        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            calculateTaxController.calculateTax(calculateTaxesDto);
        });
        assertEquals("O ID do tipo de imposto não pode ser nulo.", exception.getMessage());
        verify(taxCalculatorService, times(1)).calculateTax(calculateTaxesDto);
    }

    @Test
    void testCalculateTax_ServiceThrowsException() {
        
        CalculateTaxesDto calculateTaxesDto = new CalculateTaxesDto(1L, 1000.0);
        when(taxCalculatorService.calculateTax(calculateTaxesDto))
            .thenThrow(new RuntimeException("Erro inesperado no serviço."));

        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            calculateTaxController.calculateTax(calculateTaxesDto);
        });
        assertEquals("Erro inesperado no serviço.", exception.getMessage());
        verify(taxCalculatorService, times(1)).calculateTax(calculateTaxesDto);
    }
}