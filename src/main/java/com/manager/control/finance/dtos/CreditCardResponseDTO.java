package com.manager.control.finance.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreditCardResponseDTO(
        Integer id,
        String name,
        String flag,
        Integer dueDate,
        Boolean active,
        Integer referenceDayPurchase
) {
}
