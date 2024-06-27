package com.manager.control.finance.dtos;

import com.manager.control.finance.interfaces.YearlyTransactionSummary;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class YearlyTransactionSummaryDTO implements YearlyTransactionSummary {
    private Integer year;
    private BigDecimal amountExpenseYear;
    private BigDecimal amountRevenuesYear;

}
