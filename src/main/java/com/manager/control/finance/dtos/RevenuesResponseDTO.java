package com.manager.control.finance.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.manager.control.finance.entities.Category;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RevenuesResponseDTO(
        Integer id,
        LocalDate receivingDate
) {
}
