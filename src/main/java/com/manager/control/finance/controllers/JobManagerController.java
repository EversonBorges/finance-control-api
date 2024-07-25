package com.manager.control.finance.controllers;

import com.manager.control.finance.dtos.BalanceDTO;
import com.manager.control.finance.dtos.JobManagerResponseDTO;
import com.manager.control.finance.entities.JobManager;
import com.manager.control.finance.services.JobManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @PutMapping
    public ResponseEntity<String> executeJob(@RequestParam int year, @RequestParam int month){
        JobManager jobManager = service.buildJobManager();
         String response =  service.executeJob(month, year, jobManager);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/balance")
    public ResponseEntity<String> executeJob(@RequestBody BalanceDTO dto){
        String response = service.executeBalance(dto.date());
        return ResponseEntity.ok(response);
    }
}
