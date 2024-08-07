package com.manager.control.finance.controllers;

import com.manager.control.finance.dtos.*;
import com.manager.control.finance.entities.Investments;
import com.manager.control.finance.services.InvestmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/investments")
public class InvestmentsController {

    @Autowired
    private InvestmentsService service;

    @PostMapping
    public ResponseEntity<InvestmentsResponseDTO> createInvestments(@RequestBody InvestmentsRequestDTO dto){
        Investments response = service.saveInvestments(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(service.convertInvestmentsToDTO(response));
    }

    @GetMapping
    public ResponseEntity<List<InvestmentsResponseDTO>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvestmentsResponseDTO> findById(@PathVariable Integer id){
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping
    public ResponseEntity<InvestmentsResponseDTO> update(@RequestBody InvestmentsRequestDTO dto){
        return ResponseEntity.ok(service.update(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reference-year-month")
    public ResponseEntity<List<InvestmentsResponseDTO>> findByReferenceYearAndReferenceMonth(@RequestParam int year, @RequestParam int month){
        return ResponseEntity.ok(service.findByReferenceYearAndReferenceMonth(year, month));
    }

    @GetMapping("/reference-year-month/{year}")
    public ResponseEntity<List<InvestmentsResponseDTO>> getExpensesByYear(@PathVariable int year) {
        return  ResponseEntity.ok(service.findAllByYear(year));
    }
}
