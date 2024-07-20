package com.manager.control.finance.services;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.manager.control.finance.Mappers.RevenuesMapper;
import com.manager.control.finance.dtos.RevenuesRequestDTO;
import com.manager.control.finance.dtos.RevenuesResponseDTO;
import com.manager.control.finance.entities.Category;
import com.manager.control.finance.entities.Expenses;
import com.manager.control.finance.entities.Revenues;
import com.manager.control.finance.exceptions.DataNotFoundException;
import com.manager.control.finance.repositories.RevenuesRepository;
import com.manager.control.finance.utils.GlobalMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RevenuesService {
    @Autowired
    private RevenuesRepository repository;

    @Autowired
    private RevenuesMapper mapper;

    @Autowired
    private CategoryService categoryService;

    public Revenues create(RevenuesRequestDTO dto) {

        var year = dto.receivingDate().getYear();
        var month = dto.receivingDate().getMonth().getValue();

        Category category = categoryService.findById(dto.category().getId());

        if("Adiantamento".equals(category.getDescription())){
            month ++;

            if(month > GlobalMessages.DECEMBER) {
                year ++;
                month = GlobalMessages.ONE;
            }
        }

        Revenues revenues = mapper.toEntity(dto);
        revenues.setReferenceMonth(month);
        revenues.setReferenceYear(year);

        return repository.save(revenues);
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

    public Optional<Revenues> hasReferenceAdvanceInMonthAndYear(int month, int year){
        return repository.findByCategoryDescriptionAndYearAndMonth(year, month);
    }

    public void updateRevenue(Revenues revenues) {
        repository.save(revenues);
    }

    public List<RevenuesResponseDTO> findByReferenceYearAndReferenceMonth(int year, int month){

        List<Revenues> revenuesList = repository.findByReferenceYearAndReferenceMonth(year, month);
        return revenuesList.stream().map(mapper::toDTO).toList();
    }

    public List<RevenuesResponseDTO> findAllByYear(int year) {
        List<Revenues> expensesList = repository.findByReferenceYear(year);
        return expensesList.stream().map(mapper::toDTO).toList();
    }
}
