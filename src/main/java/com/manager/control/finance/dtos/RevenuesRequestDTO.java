package com.manager.control.finance.dtos;

import com.manager.control.finance.entities.Category;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RevenuesRequestDTO(
        Integer id,
        BigDecimal amount,
        LocalDate receivingDate,
        Category category
) {
}