package com.catalisa.taxes_api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CalculateTaxesDto {
    @NotNull
    @NotBlank
    private Long tipoImpostoId;

    @NotNull
    private double valorBase;

}
