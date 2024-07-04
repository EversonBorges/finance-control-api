package com.manager.control.finance.services;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.manager.control.finance.Mappers.BudgetedMapper;
import com.manager.control.finance.dtos.BudgetedRequestDTO;
import com.manager.control.finance.dtos.BudgetedResponseDTO;
import com.manager.control.finance.entities.Budgeted;
import com.manager.control.finance.exceptions.DataNotFoundException;
import com.manager.control.finance.repositories.BudgetedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BudgetedService {

    @Autowired
    private BudgetedMapper mapper;

    @Autowired
    private BudgetedRepository repository;

    public BudgetedResponseDTO convertBudgetedToDTO(Budgeted Budgeted){
        return mapper.toDTO(Budgeted);
    }

    public Budgeted saveBudgeted(BudgetedRequestDTO dto){
        return repository.save(mapper.toEntity(dto));
    }

    public BudgetedResponseDTO findById(Integer id) {
        Optional<Budgeted> result = repository.findById(id);

        return mapper.toDTO(result.orElseThrow(DataNotFoundException::new));
    }

    public List<BudgetedResponseDTO> findAll() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public BudgetedResponseDTO update(BudgetedRequestDTO dto) {
        Optional<Budgeted> result = repository.findById(dto.id());

        if (result.isPresent()){
            Budgeted budgeted = result.get();
            try {
                Budgeted updated = repository.save(mapper.update(budgeted,dto));
                return mapper.toDTO(updated);
            } catch (JsonMappingException e) {
                throw new RuntimeException(e);
            }
        }else{
            throw new DataNotFoundException();
        }
    }
}
