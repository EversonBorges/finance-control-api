package com.manager.control.finance.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.manager.control.finance.entities.Category;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BudgetedResponseDTO(
        LocalDate registerDate,
        int referenceYear,
        int referenceMonth,
        BigDecimal budgetedAmount,
        Category category
) {
}
