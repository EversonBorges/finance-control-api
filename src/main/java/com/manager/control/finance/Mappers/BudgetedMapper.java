package com.manager.control.finance.Mappers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.control.finance.dtos.BudgetedRequestDTO;
import com.manager.control.finance.dtos.BudgetedResponseDTO;
import com.manager.control.finance.entities.Budgeted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetedMapper {

    @Autowired
    private ObjectMapper objectMapper;

    public BudgetedResponseDTO toDTO(Budgeted budgeted) {
        return objectMapper.convertValue(budgeted, BudgetedResponseDTO.class);
    }

    public Budgeted toEntity(BudgetedRequestDTO budgetedRequestDTO) {
        return objectMapper.convertValue(budgetedRequestDTO, Budgeted.class);
    }

    public Budgeted update(Budgeted budgeted, BudgetedRequestDTO dto) throws JsonMappingException {
        return objectMapper.updateValue(budgeted, dto);
    }
}
