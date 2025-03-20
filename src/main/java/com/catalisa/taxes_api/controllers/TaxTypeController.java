package com.catalisa.taxes_api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catalisa.taxes_api.dtos.TaxRegisterDto;
import com.catalisa.taxes_api.model.Tax;
import com.catalisa.taxes_api.services.TaxTypeService;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

import java.util.List;

import javax.naming.NameAlreadyBoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@AllArgsConstructor
@RestController
@RequestMapping("/tipos")
public class TaxTypeController {

    private TaxTypeService taxTypeService;

    @PostMapping
    public ResponseEntity<Tax> registerTaxType(@RequestBody TaxRegisterDto taxRegisterDto) throws NameAlreadyBoundException {
        Tax createdTaxType = taxTypeService.registerTaxType(taxRegisterDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTaxType);
    }

    @GetMapping
    public ResponseEntity<List<Tax>> listTaxTypes() {
        List<Tax> taxTypes = taxTypeService.listAllTaxTypes();
        return ResponseEntity.status(HttpStatus.OK).body(taxTypes);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Tax> getTaxById(@PathVariable Long id) {
        Tax tax = taxTypeService.getTaxById(id);
        return ResponseEntity.status(HttpStatus.OK).body(tax);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    
}
