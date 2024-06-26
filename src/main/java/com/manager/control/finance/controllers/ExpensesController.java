package com.manager.control.finance.controllers;

import com.manager.control.finance.dtos.*;
import com.manager.control.finance.entities.Expenses;
import com.manager.control.finance.entities.PaymentMethods;
import com.manager.control.finance.services.CreditCardService;
import com.manager.control.finance.services.ExpensesService;
import com.manager.control.finance.services.PaymentMethodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/expense")
public class ExpensesController {

    @Autowired
    private ExpensesService service;

    @Autowired
    private PaymentMethodsService paymentMethodsService;

    @PostMapping
    public ResponseEntity<ResponseMessage> create(@RequestBody ExpensesRequestDTO dto){
        ResponseMessage response = service.create(dto);
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
    public ResponseEntity<ExpensesResponseDTO> update(@RequestBody ExpensesRequestDTO dto){
        return ResponseEntity.ok(service.update(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/payments-methods")
    public ResponseEntity<PaymentMethodsResponseDTO> createCategory(@RequestBody PaymentMethodsRequestDTO dto){
        PaymentMethods response = paymentMethodsService.savePaymentMethods(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(paymentMethodsService.convertPaymentMethodsToDTO(response));
    }
}
