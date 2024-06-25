package com.manager.control.finance.Mappers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.control.finance.dtos.RevenuesRequestDTO;
import com.manager.control.finance.dtos.RevenuesResponseDTO;
import com.manager.control.finance.entities.Revenues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RevenuesMapper {

    @Autowired
    private ObjectMapper objectMapper;

    public RevenuesResponseDTO toDTO(Revenues revenues) {
        return objectMapper.convertValue(revenues, RevenuesResponseDTO.class);
    }

    public Revenues toEntity(RevenuesRequestDTO revenuesRequestDTO) {
        return objectMapper.convertValue(revenuesRequestDTO, Revenues.class);
    }

    public Revenues update(Revenues revenues, RevenuesRequestDTO dto) throws JsonMappingException {
        return objectMapper.updateValue(revenues, dto);
    }
}
