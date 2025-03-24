package com.catalisa.services;

import com.catalisa.taxes_api.dtos.TaxRegisterDto;
import com.catalisa.taxes_api.model.Tax;
import com.catalisa.taxes_api.repositories.TaxRepository;
import com.catalisa.taxes_api.services.TaxTypeService;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.naming.NameAlreadyBoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaxTypeServiceTest {

    @Mock
    private TaxRepository taxRepository;

    @InjectMocks
    private TaxTypeService taxTypeService;

    public TaxTypeServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRegisterTaxType() throws NameAlreadyBoundException {
        TaxRegisterDto taxRegisterDto = new TaxRegisterDto();
        taxRegisterDto.setNome("Imposto Teste");
        taxRegisterDto.setDescricao("Descrição Teste");
        taxRegisterDto.setAliquota(10.0);

        when(taxRepository.existsByNome(taxRegisterDto.getNome())).thenReturn(false);

        Tax savedTax = new Tax();
        savedTax.setId(1L);
        savedTax.setNome(taxRegisterDto.getNome());
        savedTax.setDescricao(taxRegisterDto.getDescricao());
        savedTax.setAliquota(taxRegisterDto.getAliquota());

        when(taxRepository.save(any(Tax.class))).thenReturn(savedTax);

        Tax result = taxTypeService.registerTaxType(taxRegisterDto);

        assertNotNull(result);
        assertEquals("Imposto Teste", result.getNome());
        assertEquals("Descrição Teste", result.getDescricao());
        assertEquals(10.0, result.getAliquota());
        verify(taxRepository, times(1)).existsByNome(taxRegisterDto.getNome());
        verify(taxRepository, times(1)).save(any(Tax.class));
    }

    @Test
    void shouldThrowNameAlreadyBoundException() {
        TaxRegisterDto taxRegisterDto = new TaxRegisterDto();
        taxRegisterDto.setNome("Imposto Teste");

        when(taxRepository.existsByNome(taxRegisterDto.getNome())).thenReturn(true);

        NameAlreadyBoundException exception = assertThrows(NameAlreadyBoundException.class, () -> {
            taxTypeService.registerTaxType(taxRegisterDto);
        });

        assertEquals("Já existe um tipo de imposto com esse nome.", exception.getMessage());
        verify(taxRepository, times(1)).existsByNome(taxRegisterDto.getNome());
        verify(taxRepository, never()).save(any());
    }

    @Test
    void shouldListAllTaxTypes() {
        Tax tax1 = new Tax();
        tax1.setId(1L);
        tax1.setNome("Imposto 1");

        Tax tax2 = new Tax();
        tax2.setId(2L);
        tax2.setNome("Imposto 2");

        when(taxRepository.findAll()).thenReturn(List.of(tax1, tax2));

        List<Tax> result = taxTypeService.listAllTaxTypes();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Imposto 1", result.get(0).getNome());
        assertEquals("Imposto 2", result.get(1).getNome());
        verify(taxRepository, times(1)).findAll();
    }

    @Test
    void shouldGetTaxById() {
        Long taxId = 1L;

        Tax tax = new Tax();
        tax.setId(taxId);
        tax.setNome("Imposto Teste");

        when(taxRepository.findById(taxId)).thenReturn(Optional.of(tax));

        Tax result = taxTypeService.getTaxById(taxId);

        assertNotNull(result);
        assertEquals(taxId, result.getId());
        assertEquals("Imposto Teste", result.getNome());
        verify(taxRepository, times(1)).findById(taxId);
    }

    @Test
    void shouldThrowEntityNotFoundException() {
        Long taxId = 1L;

        when(taxRepository.findById(taxId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            taxTypeService.getTaxById(taxId);
        });

        assertEquals("Tipo de imposto com ID " + taxId + " não encontrado.", exception.getMessage());
        verify(taxRepository, times(1)).findById(taxId);
    }

    @Test
    void shouldUpdateTaxById() {
        Long taxId = 1L;
        Map<String, Object> updates = Map.of("nome", "Novo Nome", "descricao", "Nova Descrição");

        Tax existingTax = new Tax();
        existingTax.setId(taxId);
        existingTax.setNome("Nome Antigo");
        existingTax.setDescricao("Descrição Antiga");

        when(taxRepository.findById(taxId)).thenReturn(Optional.of(existingTax));
        when(taxRepository.save(existingTax)).thenReturn(existingTax);

        Tax updatedTax = taxTypeService.updateTaxById(taxId, updates);

        assertNotNull(updatedTax);
        assertEquals("Novo Nome", updatedTax.getNome());
        assertEquals("Nova Descrição", updatedTax.getDescricao());
        verify(taxRepository, times(1)).findById(taxId);
        verify(taxRepository, times(1)).save(existingTax);
    }

    @Test
    void shouldDeleteTaxById() {
        Long taxId = 1L;

        Tax existingTax = new Tax();
        existingTax.setId(taxId);
        existingTax.setNome("Imposto a ser deletado");

        when(taxRepository.findById(taxId)).thenReturn(Optional.of(existingTax));

        Tax deletedTax = taxTypeService.deleteTaxTypeById(taxId);

        assertNotNull(deletedTax);
        assertEquals(taxId, deletedTax.getId());
        assertEquals("Imposto a ser deletado", deletedTax.getNome());
        verify(taxRepository, times(1)).findById(taxId);
        verify(taxRepository, times(1)).delete(existingTax);
    }
}