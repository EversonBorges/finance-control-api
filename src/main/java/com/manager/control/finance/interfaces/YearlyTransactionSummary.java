package com.manager.control.finance.interfaces;

import java.math.BigDecimal;

public interface YearlyTransactionSummary {
    Integer getYear();
    BigDecimal getAmountExpenseYear();
    BigDecimal getAmountRevenuesYear();
}
