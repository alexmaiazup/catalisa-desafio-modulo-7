package com.catalisa.taxes_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaxTypeUpdateDto {
    private String nome;
    private String descricao;
    private double aliquota;
}
