package com.manager.control.finance.controllers;

import com.manager.control.finance.dtos.*;
import com.manager.control.finance.services.CommonsService;
import com.manager.control.finance.services.ExpensesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expense")
public class ExpensesController {

    @Autowired
    private ExpensesService service;

    @PostMapping
    public ResponseEntity<ResponseMessageDTO> create(@RequestBody ExpensesRequestDTO dto){
        ResponseMessageDTO response = service.create(dto);
        return ResponseEntity.created(null).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ExpensesResponseDTO>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpensesResponseDTO> findById(@PathVariable Integer id){
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping
    public ResponseEntity<ResponseMessageDTO> update(@RequestBody ExpensesRequestDTO dto){
        return ResponseEntity.ok(service.update(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/financial-summary")
    public List<YearlyTransactionSummaryDTO> getFinancialSummary() {
        return service.getYearlyTransactionSummary();
    }

    @GetMapping("/reference-year-month")
    public ResponseEntity<List<ExpensesResponseDTO>>  getExpensesByMonthsByYear(@RequestParam int year, @RequestParam int month) {
        return ResponseEntity.ok(service.findByReferenceYearAndReferenceMonth(year, month));
    }

    @GetMapping("/reference-year-month/{year}")
    public ResponseEntity<List<ExpensesResponseDTO>> getExpensesByYear(@PathVariable int year) {
        return  ResponseEntity.ok(service.findAllByYear(year));
    }
}
