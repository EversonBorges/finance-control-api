package com.manager.control.finance.Mappers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.control.finance.dtos.CreditCardRequestDTO;
import com.manager.control.finance.dtos.CreditCardResponseDTO;
import com.manager.control.finance.entities.CreditCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditCardMapper {

    @Autowired
    private ObjectMapper objectMapper;

    public CreditCardResponseDTO toDTO(CreditCard creditCard) {
        return objectMapper.convertValue(creditCard, CreditCardResponseDTO.class);
    }

    public CreditCard toEntity(CreditCardRequestDTO creditCardRequestDTO) {
        return objectMapper.convertValue(creditCardRequestDTO, CreditCard.class);
    }

    public CreditCard update(CreditCard creditCard, CreditCardRequestDTO dto) throws JsonMappingException {
        return objectMapper.updateValue(creditCard, dto);
    }
}
