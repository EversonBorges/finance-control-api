package com.manager.control.finance.controllers;

import com.manager.control.finance.dtos.CreditCardRequestDTO;
import com.manager.control.finance.dtos.CreditCardResponseDTO;
import com.manager.control.finance.dtos.PaymentMethodsRequestDTO;
import com.manager.control.finance.dtos.PaymentMethodsResponseDTO;
import com.manager.control.finance.entities.CreditCard;
import com.manager.control.finance.entities.PaymentMethods;
import com.manager.control.finance.services.CreditCardService;
import com.manager.control.finance.services.PaymentMethodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/credit-card")
public class CreditCardController {

    @Autowired
    private CreditCardService service;

    @PostMapping
    public ResponseEntity<CreditCardResponseDTO> create(@RequestBody CreditCardRequestDTO dto){
        CreditCard response = service.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(service.convertCreditCardToDTO(response));
    }

    @GetMapping
    public ResponseEntity<List<CreditCardResponseDTO>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditCardResponseDTO> findById(@PathVariable Integer id){
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping
    public ResponseEntity<CreditCardResponseDTO> update(@RequestBody CreditCardRequestDTO dto){
        return ResponseEntity.ok(service.update(dto));
    }

}
