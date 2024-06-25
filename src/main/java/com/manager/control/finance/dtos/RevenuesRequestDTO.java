package com.manager.control.finance.dtos;

import com.manager.control.finance.entities.Category;

import java.time.LocalDate;

public record RevenuesRequestDTO(
        Integer id,
        Double amount,
        LocalDate receivingDate,
        Category category
) {
}
