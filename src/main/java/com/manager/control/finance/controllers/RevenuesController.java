package com.manager.control.finance.controllers;

import com.manager.control.finance.dtos.ExpensesResponseDTO;
import com.manager.control.finance.dtos.RevenuesRequestDTO;
import com.manager.control.finance.dtos.RevenuesResponseDTO;
import com.manager.control.finance.entities.Revenues;
import com.manager.control.finance.services.RevenuesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/revenues")
public class RevenuesController {

    @Autowired
    private RevenuesService service;

    @PostMapping
    public ResponseEntity<RevenuesResponseDTO> create(@RequestBody RevenuesRequestDTO dto){
        Revenues response = service.create(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(service.convertRevenuesToDTO(response));
    }

    @GetMapping
    public ResponseEntity<List<RevenuesResponseDTO>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RevenuesResponseDTO> findById(@PathVariable Integer id){
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping
    public ResponseEntity<RevenuesResponseDTO> update(@RequestBody RevenuesRequestDTO dto){
        return ResponseEntity.ok(service.update(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reference-year-month")
    public ResponseEntity<List<RevenuesResponseDTO>> findByReferenceYearAndReferenceMonth(@RequestParam int year, @RequestParam int month){
        return ResponseEntity.ok(service.findByReferenceYearAndReferenceMonth(year, month));
    }

    @GetMapping("/reference-year-month/{year}")
    public ResponseEntity<List<RevenuesResponseDTO>> getExpensesByYear(@PathVariable int year) {
        return  ResponseEntity.ok(service.findAllByYear(year));
    }
}
