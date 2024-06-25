package com.manager.control.finance.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.manager.control.finance.entities.Category;
import com.manager.control.finance.entities.PaymentMethods;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreditCardRequestDTO(
         Integer id,
         String name,
         String flag,
         Integer dueDate,
         Boolean active,
         Integer referenceDayPurchase
) {
}
