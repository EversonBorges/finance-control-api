package com.manager.control.finance.dtos;

import com.manager.control.finance.entities.Category;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BudgetedRequestDTO(
        Integer id,
        LocalDate registerDate,
        int referenceYear,
        int referenceMonth,
        BigDecimal budgetedAmount,
        Category category
) {
}