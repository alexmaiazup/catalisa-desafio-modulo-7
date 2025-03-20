package com.catalisa.controllers;

import com.catalisa.config.TestSecurityConfig;
import com.catalisa.taxes_api.controllers.TaxTypeController;
import com.catalisa.taxes_api.dtos.TaxRegisterDto;
import com.catalisa.taxes_api.model.Tax;
import com.catalisa.taxes_api.services.TaxTypeService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaxTypeController.class)
@ContextConfiguration(classes = {TestSecurityConfig.class, TaxTypeController.class})
public class TaxTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
    public void shouldReturnNotFoundWhenTaxByIdDoesNotExist() throws Exception {
        when(taxTypeService.getTaxById(999L)).thenThrow(new EntityNotFoundException("Tipo de imposto com ID 999 não encontrado."));

        mockMvc.perform(get("/tipos/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Tipo de imposto com ID 999 não encontrado."));
    }
}