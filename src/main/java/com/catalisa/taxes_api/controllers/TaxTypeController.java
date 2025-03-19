package com.catalisa.taxes_api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catalisa.taxes_api.dtos.TaxRegisterDto;
import com.catalisa.taxes_api.services.TaxTypeService;

import lombok.AllArgsConstructor;

import javax.naming.NameAlreadyBoundException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@AllArgsConstructor
@RestController
@RequestMapping("/tipos")
public class TaxTypeController {

    private TaxTypeService taxTypeService;

    @PostMapping("path")
    public void registerTaxType(@RequestBody TaxRegisterDto taxRegisterDto) throws NameAlreadyBoundException {
        
        taxTypeService.registerTaxType(taxRegisterDto);
    }
    
}
