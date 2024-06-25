package com.manager.control.finance.Mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.control.finance.dtos.CategoryRequestDTO;
import com.manager.control.finance.dtos.CategoryResponseDTO;
import com.manager.control.finance.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryMapper {

    @Autowired
    private ObjectMapper objectMapper;

    public CategoryResponseDTO toDTO(Category category) {
        return objectMapper.convertValue(category, CategoryResponseDTO.class);
    }

    public Category toEntity(CategoryRequestDTO categoryRequestDTO) {
        return objectMapper.convertValue(categoryRequestDTO, Category.class);
    }
}
