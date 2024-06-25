package com.manager.control.finance.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.manager.control.finance.entities.Category;
import com.manager.control.finance.entities.PaymentMethods;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExpensesResponseDTO(
        Integer id,
        String establishment,
        LocalDate purchaseDate
) {
}
