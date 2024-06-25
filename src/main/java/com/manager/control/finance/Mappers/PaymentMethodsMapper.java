package com.manager.control.finance.Mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.control.finance.dtos.PaymentMethodsRequestDTO;
import com.manager.control.finance.dtos.PaymentMethodsResponseDTO;
import com.manager.control.finance.entities.PaymentMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentMethodsMapper {

    @Autowired
    private ObjectMapper objectMapper;

    public PaymentMethodsResponseDTO toDTO(PaymentMethods paymentMethods) {
        return objectMapper.convertValue(paymentMethods, PaymentMethodsResponseDTO.class);
    }

    public PaymentMethods toEntity(PaymentMethodsRequestDTO paymentMethodsRequestDTO) {
        return objectMapper.convertValue(paymentMethodsRequestDTO, PaymentMethods.class);
    }
}
