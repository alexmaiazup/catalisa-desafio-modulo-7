package com.catalisa.taxes_api.dtos;

import lombok.Data;

@Data
public class TaxRegisterDto {
    private String nome;
    private String descricao;
    private double aliquota;
}
