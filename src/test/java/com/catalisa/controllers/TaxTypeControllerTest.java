package com.catalisa.controllers;

import com.catalisa.config.TestSecurityConfig;
import com.catalisa.taxes_api.controllers.TaxTypeController;
import com.catalisa.taxes_api.dtos.TaxRegisterDto;
import com.catalisa.taxes_api.model.Tax;
import com.catalisa.taxes_api.services.TaxTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaxTypeController.class)
@ContextConfiguration(classes = {TestSecurityConfig.class, TaxTypeController.class})
public class TaxTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaxTypeService taxTypeService;

    @Test
    public void shouldListAllTaxTypes() throws Exception {
        Tax tax1 = new Tax();
        tax1.setId(1L);
        tax1.setNome("ICMS");
        tax1.setDescricao("Imposto sobre circulação de mercadorias");
        tax1.setAliquota(18.0);

        Tax tax2 = new Tax();
        tax2.setId(2L);
        tax2.setNome("ISS");
        tax2.setDescricao("Imposto sobre serviços");
        tax2.setAliquota(5.0);

        List<Tax> mockTaxList = Arrays.asList(tax1, tax2);

        when(taxTypeService.listAllTaxTypes()).thenReturn(mockTaxList);

        mockMvc.perform(get("/tipos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("ICMS"))
                .andExpect(jsonPath("$[0].descricao").value("Imposto sobre circulação de mercadorias"))
                .andExpect(jsonPath("$[0].aliquota").value(18.0))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].nome").value("ISS"))
                .andExpect(jsonPath("$[1].descricao").value("Imposto sobre serviços"))
                .andExpect(jsonPath("$[1].aliquota").value(5.0));
    }

    @Test
    public void shouldRegisterTaxType() throws Exception {
        Tax tax = new Tax();
        tax.setId(1L);
        tax.setNome("ICMS");
        tax.setDescricao("Imposto sobre circulação de mercadorias");
        tax.setAliquota(18.0);

        when(taxTypeService.registerTaxType(any(TaxRegisterDto.class))).thenReturn(tax);

        mockMvc.perform(post("/tipos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\": \"ICMS\", \"descricao\": \"Imposto sobre circulação de mercadorias\", \"aliquota\": 18.0}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("ICMS"))
                .andExpect(jsonPath("$.descricao").value("Imposto sobre circulação de mercadorias"))
                .andExpect(jsonPath("$.aliquota").value(18.0));
    }

    @Test
    public void shouldGetTaxById() throws Exception {
        Tax tax = new Tax();
        tax.setId(1L);
        tax.setNome("ICMS");
        tax.setDescricao("Imposto sobre circulação de mercadorias");
        tax.setAliquota(18.0);

        when(taxTypeService.getTaxById(1L)).thenReturn(tax);

        mockMvc.perform(get("/tipos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("ICMS"))
                .andExpect(jsonPath("$.descricao").value("Imposto sobre circulação de mercadorias"))
                .andExpect(jsonPath("$.aliquota").value(18.0));
    }

    @Test
    public void shouldReturnNotFoundForNonExistentTaxType() throws Exception {
        
        Long nonExistentId = 999L;
        String expectedMessage = "Tipo de imposto com ID " + nonExistentId + " não encontrado.";

        when(taxTypeService.getTaxById(nonExistentId)).thenThrow(new EntityNotFoundException(expectedMessage));

        
        mockMvc.perform(get("/tipos/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateTaxById() throws Exception {

        Long taxId = 1L;
        Map<String, Object> updates = Map.of("nome", "Novo Nome", "descricao", "Nova Descrição");
        Tax updatedTax = new Tax();
        updatedTax.setId(taxId);
        updatedTax.setNome("Novo Nome");
        updatedTax.setDescricao("Nova Descrição");

        Mockito.when(taxTypeService.updateTaxById(eq(taxId), Mockito.<Map<String, Object>>any()))
       .thenReturn(updatedTax);

        mockMvc.perform(put("/tipos/{id}", taxId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taxId))
                .andExpect(jsonPath("$.nome").value("Novo Nome"))
                .andExpect(jsonPath("$.descricao").value("Nova Descrição"));
    }

    @Test
    void testDeleteTaxTypeById() throws Exception {

        Long taxId = 1L;
        Tax deletedTax = new Tax();
        deletedTax.setId(taxId);
        deletedTax.setNome("Imposto Deletado");

        Mockito.when(taxTypeService.deleteTaxTypeById(taxId)).thenReturn(deletedTax);

        mockMvc.perform(delete("/tipos/{id}", taxId))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(taxId))
                .andExpect(jsonPath("$.nome").value("Imposto Deletado"));
    }
}