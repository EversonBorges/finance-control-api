package com.manager.control.finance.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlySumSummaryDTO {
    private BigDecimal sum;
    private int referenceMonth;
    private String description;

    public MonthlySumSummaryDTO(BigDecimal sum, int referenceMonth) {
        this.sum = sum;
        this.referenceMonth = referenceMonth;
    }
}
