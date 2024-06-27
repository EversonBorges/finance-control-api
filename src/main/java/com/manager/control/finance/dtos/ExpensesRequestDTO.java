package com.manager.control.finance.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.manager.control.finance.entities.Category;
import com.manager.control.finance.entities.CreditCard;
import jakarta.persistence.Column;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExpensesRequestDTO(
        Integer id,
        String establishment,
        Integer numberInstallment,
        Integer quantityInstallments,
        BigDecimal valuesInstallment,
        LocalDate transactionDate,
        Category category,
        String paymentMethods,
        CreditCard creditCard
) {
}
