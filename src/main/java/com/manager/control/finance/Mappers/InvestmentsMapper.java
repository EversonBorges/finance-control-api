package com.manager.control.finance.Mappers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.control.finance.dtos.InvestmentsRequestDTO;
import com.manager.control.finance.dtos.InvestmentsResponseDTO;
import com.manager.control.finance.entities.Investments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvestmentsMapper {

    @Autowired
    private ObjectMapper objectMapper;

    public InvestmentsResponseDTO toDTO(Investments investments) {
        return objectMapper.convertValue(investments, InvestmentsResponseDTO.class);
    }

    public Investments toEntity(InvestmentsRequestDTO investmentsRequestDTO) {
        return objectMapper.convertValue(investmentsRequestDTO, Investments.class);
    }

    public Investments update(Investments investments, InvestmentsRequestDTO dto) throws JsonMappingException {
        return objectMapper.updateValue(investments, dto);
    }
}
