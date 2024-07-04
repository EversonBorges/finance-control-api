package com.manager.control.finance.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlySummaryDTO {
    private BigDecimal sum;
    private int referenceMonth;
    private String descriptionCategory;
}
