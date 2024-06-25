package com.manager.control.finance.Mappers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.control.finance.dtos.ExpensesRequestDTO;
import com.manager.control.finance.dtos.ExpensesResponseDTO;
import com.manager.control.finance.entities.Expenses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpensesMapper {

    @Autowired
    private ObjectMapper objectMapper;

    public ExpensesResponseDTO toDTO(Expenses expenses) {
        return objectMapper.convertValue(expenses, ExpensesResponseDTO.class);
    }

    public Expenses toEntity(ExpensesRequestDTO expensesRequestDTO) {
        return objectMapper.convertValue(expensesRequestDTO, Expenses.class);
    }

    public Expenses update(Expenses expenses, ExpensesRequestDTO dto) throws JsonMappingException {
        return objectMapper.updateValue(expenses, dto);
    }
}
