package com.manager.control.finance.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.manager.control.finance.entities.Category;
import com.manager.control.finance.entities.CreditCard;
import com.manager.control.finance.enums.PaymentMethodsEnum;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExpensesResponseDTO(
        Integer id,
        String establishment,
        Integer numberInstallment,
        Integer quantityInstallments,
        BigDecimal valuesInstallment,
        String week,
        LocalDate transactionDate,
        Integer referenceMonth,
        Integer referenceYear,
        Category category,
        CreditCard creditCard,
        String paymentMethods
        ) {

}


