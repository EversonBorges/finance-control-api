package com.manager.control.finance.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
