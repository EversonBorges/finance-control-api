package com.manager.control.finance.services;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.manager.control.finance.Mappers.RevenuesMapper;
import com.manager.control.finance.dtos.RevenuesRequestDTO;
import com.manager.control.finance.dtos.RevenuesResponseDTO;
import com.manager.control.finance.entities.Revenues;
import com.manager.control.finance.exceptions.DataNotFoundException;
import com.manager.control.finance.repositories.RevenuesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RevenuesService {
    @Autowired
    private RevenuesRepository repository;

    @Autowired
    private RevenuesMapper mapper;

    public Revenues create(RevenuesRequestDTO dto) {
        return repository.save(mapper.toEntity(dto));
    }

    public RevenuesResponseDTO convertRevenuesToDTO(Revenues revenues) {
        return mapper.toDTO(revenues);
    }

    public List<RevenuesResponseDTO> findAll() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    public RevenuesResponseDTO findById(Integer id) {
        Optional<Revenues> result = repository.findById(id);

        return mapper.toDTO(result.orElseThrow(DataNotFoundException::new));
    }

    public RevenuesResponseDTO update(RevenuesRequestDTO dto) {
        Optional<Revenues> result = repository.findById(dto.id());

         if (result.isPresent()){
             Revenues revenues = result.get();
             try {
                 Revenues updated = repository.save(mapper.update(revenues,dto));
                 return mapper.toDTO(updated);
             } catch (JsonMappingException e) {
                 throw new RuntimeException(e);
             }
         }else{
             throw new DataNotFoundException();
         }
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public Boolean hasReferenceAdvanceInMonthAndYear(int month, int year){
        Optional<Revenues> result = repository.findByCategoryDescriptionAndYearAndMonth(month, year);
        return result.isPresent();
    }
}
