package com.manager.control.finance.services;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.manager.control.finance.Mappers.ExpensesMapper;
import com.manager.control.finance.dtos.ExpensesRequestDTO;
import com.manager.control.finance.dtos.ExpensesResponseDTO;
import com.manager.control.finance.entities.CreditCard;
import com.manager.control.finance.entities.Expenses;
import com.manager.control.finance.exceptions.DataNotFoundException;
import com.manager.control.finance.repositories.ExpensesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class ExpensesService {

    @Autowired
    private ExpensesRepository repository;

    @Autowired
    private ExpensesMapper mapper;

    @Autowired
    private CreditCardService creditCardService;

    public Expenses create(ExpensesRequestDTO dto){
        String week = buildWeekMonth(dto.purchaseDate());
        Expenses expenses = mapper.toEntity(dto);
        expenses.setWeek(week);
        return repository.save(expenses);
    }

    private void verifyBestDayOfBuyCreditCard(ExpensesRequestDTO dto, Expenses expenses){
        CreditCard creditCard = creditCardService.findCreditCardById(dto.creditCard().getId());
        var dueDay = creditCard.getDueDate();
        var referenceDayPurchase = creditCard.getReferenceDayPurchase();

        LocalDate now = LocalDate.now();
        LocalDate dueDate ;

        if(now.getDayOfMonth() >= dueDay){
            dueDate = LocalDate.of(now.getYear(),now.getMonth().plus(1), dueDay);
        }else {
            dueDate = LocalDate.of(now.getYear(),now.getMonth(), dueDay);
        }

        LocalDate bestOfDayPurchase = dueDate.minusDays(referenceDayPurchase);

        if(dto.purchaseDate().isBefore(bestOfDayPurchase.minusDays(1))){
            expenses.setReferenceYear(dto.purchaseDate().getYear());
            expenses.setReferenceMonth(dto.purchaseDate().getMonth().getValue());
        }else{
            buildReferenceYearAndMonth(dto, expenses);
        }
    }

    private static void buildReferenceYearAndMonth(ExpensesRequestDTO dto, Expenses expenses) {
        var referenceMonth = dto.purchaseDate().getMonth().plus(1).getValue();
        expenses.setReferenceMonth(referenceMonth);

        if(referenceMonth < dto.purchaseDate().getMonth().getValue()){
            expenses.setReferenceYear(dto.purchaseDate().plusYears(1).getYear());
        }else{
            expenses.setReferenceYear(dto.purchaseDate().getYear());
        }
    }

    public List<ExpensesResponseDTO> findAll() {
        List<Expenses> resultList = repository.findAll();

        return  resultList.stream().map(mapper::toDTO).toList();
    }

    public ExpensesResponseDTO findById(Integer id)  {
        Optional<Expenses> result = repository.findById(id);
        return  mapper.toDTO(result.orElseThrow(DataNotFoundException::new));
    }

    public ExpensesResponseDTO update(ExpensesRequestDTO dto) {
        Optional<Expenses> result = repository.findById(dto.id());

        if (result.isPresent()){
            Expenses expenses = result.get();
            try {
                Expenses updated = repository.save(mapper.update(expenses,dto));
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

    public ExpensesResponseDTO convertExpensesToDTO(Expenses expenses){
        return mapper.toDTO(expenses);
    }

    private String buildWeekMonth(LocalDate date){
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int weekOfMonth = date.get(weekFields.weekOfMonth());

        return "Semana " + weekOfMonth;
    }
}
