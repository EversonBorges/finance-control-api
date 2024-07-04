package com.manager.control.finance.controllers;

import com.manager.control.finance.dtos.BudgetedRequestDTO;
import com.manager.control.finance.dtos.BudgetedResponseDTO;
import com.manager.control.finance.entities.Budgeted;
import com.manager.control.finance.services.BudgetedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/budgeted")
public class BudgetedController {

    @Autowired
    private BudgetedService service;

    @PostMapping
    public ResponseEntity<BudgetedResponseDTO> createBudgeted(@RequestBody BudgetedRequestDTO dto){
        Budgeted response = service.saveBudgeted(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(service.convertBudgetedToDTO(response));
    }

    @GetMapping
    public ResponseEntity<List<BudgetedResponseDTO>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetedResponseDTO> findById(@PathVariable Integer id){
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping
    public ResponseEntity<BudgetedResponseDTO> update(@RequestBody BudgetedRequestDTO dto){
        return ResponseEntity.ok(service.update(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
