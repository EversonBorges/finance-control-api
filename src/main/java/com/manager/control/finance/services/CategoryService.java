package com.manager.control.finance.services;

import com.manager.control.finance.Mappers.CategoryMapper;
import com.manager.control.finance.dtos.CategoryRequestDTO;
import com.manager.control.finance.dtos.CategoryResponseDTO;
import com.manager.control.finance.entities.Category;
import com.manager.control.finance.exceptions.DataNotFoundException;
import com.manager.control.finance.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
