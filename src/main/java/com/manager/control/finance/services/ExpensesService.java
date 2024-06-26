package com.manager.control.finance.services;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.manager.control.finance.Mappers.ExpensesMapper;
import com.manager.control.finance.dtos.ExpensesRequestDTO;
import com.manager.control.finance.dtos.ExpensesResponseDTO;
import com.manager.control.finance.dtos.ReferenceMonthYearExpenses;
import com.manager.control.finance.dtos.ResponseMessage;
import com.manager.control.finance.entities.CreditCard;
import com.manager.control.finance.entities.Expenses;
import com.manager.control.finance.enums.PaymentMethodsEnum;
import com.manager.control.finance.exceptions.DataNotFoundException;
import com.manager.control.finance.repositories.ExpensesRepository;
import com.manager.control.finance.utils.GlobalMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
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

    public ResponseMessage create(ExpensesRequestDTO dto){

        List<Expenses> expensesList = new ArrayList<>();
        var year = dto.purchaseDate().getYear();
        var month = dto.purchaseDate().getMonth().getValue();

        if(dto.paymentMethods().equals(PaymentMethodsEnum.CREDIT_CARD.getId())){
            ReferenceMonthYearExpenses reference = verifyBestDayOfBuyCreditCard(dto);
             year = reference.getYear();
             month = reference.getMonth();
        }

        for (int i = 0; i < dto.quantityInstallments(); i++) {

            String week = buildWeekMonth(dto.purchaseDate());
            Expenses expenses = mapper.toEntity(dto);
            expenses.setReferenceYear(year);
            expenses.setReferenceMonth(month);
            expenses.setNumberInstallment(i + GlobalMessages.ONE);
            expenses.setWeek(week);

            month ++;

            if(month > GlobalMessages.DECEMBER) {
                year ++;
                month = GlobalMessages.ONE;
            }
            expensesList.add(expenses);
        }

        repository.saveAll(expensesList);
        return new ResponseMessage(GlobalMessages.MSG_SUCCESS);
    }

    private ReferenceMonthYearExpenses verifyBestDayOfBuyCreditCard(ExpensesRequestDTO dto){
        ReferenceMonthYearExpenses referenceMonthYearExpenses = new ReferenceMonthYearExpenses();
        CreditCard creditCard = creditCardService.findCreditCardById(dto.creditCard().getId());
        var dueDay = creditCard.getDueDate();
        var referenceDayPurchase = creditCard.getReferenceDayPurchase();

        var year = checkYearIncrement(dto.purchaseDate());
        var dueDate = LocalDate.of(year, dto.purchaseDate().getMonth().plus(GlobalMessages.ONE).getValue(), dueDay);
        LocalDate bestOfDayPurchase = dueDate.minusDays(referenceDayPurchase);
        long diffDays = ChronoUnit.DAYS.between(dto.purchaseDate(), bestOfDayPurchase);

        if(diffDays > GlobalMessages.MAX_DAY){
            dueDate = LocalDate.of(year, dto.purchaseDate().getMonth().getValue(), dueDay);
        }

        if(dto.purchaseDate().isBefore(bestOfDayPurchase)){

            referenceMonthYearExpenses.setMonth(dueDate.getMonth().getValue());
            referenceMonthYearExpenses.setYear(dueDate.getYear());
        }else{
            referenceMonthYearExpenses.setMonth(dueDate.getMonth().plus(GlobalMessages.ONE).getValue());
            var dueYear = checkYearIncrement(dueDate);
            referenceMonthYearExpenses.setYear(dueYear);

        }

        return referenceMonthYearExpenses;
    }

    private static int checkYearIncrement(LocalDate date) {
        int year;
        if(date.getMonth().getValue() == GlobalMessages.DECEMBER){
            year = date.getYear() + GlobalMessages.ONE;
        }else {
            year = date.getYear();
        }
        return year;
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

    private String buildWeekMonth(LocalDate date){
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int weekOfMonth = date.get(weekFields.weekOfMonth());

        return GlobalMessages.WEEK + weekOfMonth;
    }
}
