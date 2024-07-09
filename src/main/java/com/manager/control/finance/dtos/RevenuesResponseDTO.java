package com.manager.control.finance.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.manager.control.finance.entities.Category;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RevenuesResponseDTO(
        Long id,
        BigDecimal amount,
        LocalDate receivingDate,
        Category category
) {
}
