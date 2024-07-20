package com.manager.control.finance.controllers;

import com.manager.control.finance.dtos.CategoryRequestDTO;
import com.manager.control.finance.dtos.CategoryResponseDTO;
import com.manager.control.finance.dtos.InvestmentsRequestDTO;
import com.manager.control.finance.dtos.InvestmentsResponseDTO;
import com.manager.control.finance.entities.Category;
import com.manager.control.finance.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody CategoryRequestDTO dto){
        Category response = categoryService.saveCategory(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(categoryService.convertCategoryToDTO(response));
    }

    @PutMapping
    public ResponseEntity<CategoryResponseDTO> update(@RequestBody CategoryRequestDTO dto){
        return ResponseEntity.ok(categoryService.update(dto));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> findAllCategory(){
        return ResponseEntity.ok(categoryService.findAllCategory());
    }

    @GetMapping("/{type}")
    public ResponseEntity<List<CategoryResponseDTO>> findAllCategoryByType(@PathVariable Character type){
        return ResponseEntity.ok(categoryService.findAllCategoryByType(type));
    }
}
