package com.manager.control.finance.services;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.manager.control.finance.Mappers.CategoryMapper;
import com.manager.control.finance.dtos.CategoryRequestDTO;
import com.manager.control.finance.dtos.CategoryResponseDTO;
import com.manager.control.finance.dtos.InvestmentsRequestDTO;
import com.manager.control.finance.dtos.InvestmentsResponseDTO;
import com.manager.control.finance.entities.Category;
import com.manager.control.finance.entities.Investments;
import com.manager.control.finance.exceptions.DataNotFoundException;
import com.manager.control.finance.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryResponseDTO> findAllCategoryByType(Character type) {

        List<Category> result = categoryRepository.findByType(type);
        return result.stream().map(categoryMapper::toDTO).toList();
    }

    public CategoryResponseDTO convertCategoryToDTO(Category category){
        return categoryMapper.toDTO(category);
    }

    public Category saveCategory(CategoryRequestDTO dto){
        return categoryRepository.save(categoryMapper.toEntity(dto));
    }

    public Category findById(Integer id) {
        return categoryRepository.findById(id).orElseThrow(DataNotFoundException::new);
    }

    public CategoryResponseDTO update(CategoryRequestDTO dto) {
        Optional<Category> result = categoryRepository.findById(dto.id());

        if (result.isPresent()){
            Category category = result.get();
            try {
                Category updated = categoryRepository.save(categoryMapper.update(category,dto));
                return categoryMapper.toDTO(updated);
            } catch (JsonMappingException e) {
                throw new RuntimeException(e);
            }
        }else{
            throw new DataNotFoundException();
        }
    }

    public List<CategoryResponseDTO> findAllCategory() {
        List<Category> result = categoryRepository.findAll();
        return result.stream().map(categoryMapper::toDTO).toList();
    }

    public Category findByDescription(String description) {
       return categoryRepository.findByDescription(description);
    }
}
