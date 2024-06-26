package com.manager.control.finance.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.manager.control.finance.entities.Category;
import com.manager.control.finance.entities.CreditCard;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExpensesRequestDTO(
        Integer id,
        String establishment,
        Integer numberInstallment,
        Integer quantityInstallments,
        Double valuesInstallment,
        LocalDate purchaseDate,
        Category category,
        String paymentMethods,
        CreditCard creditCard
) {
}
