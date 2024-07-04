package com.manager.control.finance.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlySumExpenseSummaryDTO {
    private int referenceMonth;
    private BigDecimal essentialSum = BigDecimal.ZERO;
    private BigDecimal nonEssentialSum = BigDecimal.ZERO;
}
