package com.catalisa.taxes_api.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxRegisterDto {
    @NotBlank(message = "O nome do imposto é obrigatório.")
    private String nome;

    @NotBlank(message = "A descrição do imposto é obrigatória.")
    private String descricao;

    @NotNull(message = "A alíquota é obrigatória.")
    @DecimalMin(value = "0.0", inclusive = false, message = "A alíquota deve ser maior que 0.")
    private Double aliquota;
}
