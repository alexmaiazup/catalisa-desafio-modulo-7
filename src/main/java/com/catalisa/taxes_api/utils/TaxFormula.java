package com.catalisa.taxes_api.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxFormula {
    private String tipoImposto;
    private double valorBase;
    private double aliquota;
    private double valorImposto;

    public TaxFormula(String tipoImposto, double valorBase, double aliquota) {
        this.tipoImposto = tipoImposto;
        this.valorBase = valorBase;
        this.aliquota = aliquota;
        this.valorImposto = calcularValorImposto();
    }

    private double calcularValorImposto() {
        return valorBase * (aliquota / 100);
    }

}
