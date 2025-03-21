package com.catalisa.taxes_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxRegisterDto {
    private String nome;
    private String descricao;
    private double aliquota;
}
