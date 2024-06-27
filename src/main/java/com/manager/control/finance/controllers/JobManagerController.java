package com.manager.control.finance.controllers;

import com.manager.control.finance.dtos.JobManagerResponseDTO;
import com.manager.control.finance.services.JobManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/job-manager")
public class JobManagerController {

    @Autowired
    private JobManagerService service;

    @GetMapping
    public ResponseEntity<List<JobManagerResponseDTO>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity<List<JobManagerResponseDTO>> findById(@PathVariable String name){
        return ResponseEntity.ok(service.findByName(name));
    }
}
