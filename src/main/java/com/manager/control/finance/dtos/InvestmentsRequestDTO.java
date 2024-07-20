package com.manager.control.finance.dtos;

import com.manager.control.finance.entities.Category;
import jakarta.persistence.Column;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InvestmentsRequestDTO(
        Integer id,
        String description,
        LocalDate transactionDate,
        BigDecimal valueInvestments,
        Category category
) {
}
