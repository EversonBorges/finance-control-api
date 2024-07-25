package com.manager.control.finance.services;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.manager.control.finance.Mappers.ExpensesMapper;
import com.manager.control.finance.dtos.*;
import com.manager.control.finance.entities.CreditCard;
import com.manager.control.finance.entities.Expenses;
import com.manager.control.finance.enums.PaymentMethodsEnum;
import com.manager.control.finance.exceptions.DataNotFoundException;
import com.manager.control.finance.repositories.ExpensesRepository;
import com.manager.control.finance.utils.GlobalMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExpensesService {

    @Autowired
    private ExpensesRepository repository;

    @Autowired
    private ExpensesMapper mapper;

    @Autowired
    private CreditCardService creditCardService;

    public ResponseMessageDTO create(ExpensesRequestDTO dto){

        List<Expenses> expensesList = new ArrayList<>();
        var year = dto.transactionDate().getYear();
        var month = dto.transactionDate().getMonth().getValue();

        if(dto.paymentMethods().equals(PaymentMethodsEnum.CREDIT_CARD.getId())){
            ReferenceMonthYearExpenses reference = verifyBestDayOfBuyCreditCard(dto);
             year = reference.getYear();
             month = reference.getMonth();
        }
        var transactionControl = UUID.randomUUID();

        for (int i = 0; i < dto.quantityInstallments(); i++) {
            Expenses expenses = mapper.toEntity(dto);
            if(expenses.getId() != null){
                expenses.setId(null);
            }
            expenses.setTransactionControl(transactionControl);
            String week = buildWeekMonth(dto.transactionDate());
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
        return new ResponseMessageDTO(GlobalMessages.MSG_SUCCESS);
    }

    private ReferenceMonthYearExpenses verifyBestDayOfBuyCreditCard(ExpensesRequestDTO dto){
        ReferenceMonthYearExpenses referenceMonthYearExpenses = new ReferenceMonthYearExpenses();
        CreditCard creditCard = creditCardService.findCreditCardById(dto.creditCard().getId());
        var dueDay = creditCard.getDueDate();
        var referenceDayPurchase = creditCard.getReferenceDayPurchase();

        var year = checkYearIncrement(dto.transactionDate());
        var dueDate = LocalDate.of(year, dto.transactionDate().getMonth().plus(GlobalMessages.ONE).getValue(), referenceDayPurchase);
        LocalDate bestOfDayPurchase = dueDate.minusDays(dueDay);
        long diffDays = ChronoUnit.DAYS.between(dto.transactionDate(), bestOfDayPurchase);

        if(diffDays > GlobalMessages.MAX_DAY){
            dueDate = LocalDate.of(year, dto.transactionDate().getMonth().getValue(), referenceDayPurchase);
        }

        if(dto.transactionDate().isBefore(bestOfDayPurchase)){

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

    public ResponseMessageDTO update(ExpensesRequestDTO dto) {
        Optional<Expenses> result = repository.findById(dto.id());

        if (result.isPresent()){
            Expenses expenses = result.get();

            if(expenses.getQuantityInstallments() == GlobalMessages.ONE && dto.quantityInstallments() == GlobalMessages.ONE){
                try {
                    Expenses resultMapper = mapper.update(expenses,dto);
                    resultMapper.setNumberInstallment(resultMapper.getQuantityInstallments());
                    repository.save(resultMapper);
                    mapper.toDTO(resultMapper);
                    return new ResponseMessageDTO(GlobalMessages.MSG_SUCCESS);
                } catch (JsonMappingException e) {
                    throw new RuntimeException(e);
                }
            }

            List<Expenses> list  = repository.findByTransactionControl(expenses.getTransactionControl());
            repository.deleteAll(list);
            create(dto);

            return new ResponseMessageDTO(GlobalMessages.MSG_SUCCESS);
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

    public List<Expenses> findAllExpensesBasedReceivingDate(LocalDate date){
        YearMonth yearMonth = YearMonth.from(date);
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

        List<PaymentMethodsEnum> paymentMethods = Arrays.asList(
                PaymentMethodsEnum.DEBIT_CARD,
                PaymentMethodsEnum.PIX,
                PaymentMethodsEnum.ACCOUNT_DISCOUNT);

        return repository.findByTransactionDateBetweenAndPaymentMethodsIn(date, lastDayOfMonth,paymentMethods);
    }

    public ResponseMessageDTO updateAllExpensesByAfterDateNow(List<Expenses> updateExpensesList){
        repository.saveAll(updateExpensesList);
        return new ResponseMessageDTO(GlobalMessages.MSG_SUCCESS);
    }

    public List<YearlyTransactionSummaryDTO> getYearlyTransactionSummary() {

        return repository.findYearlyTransactionSummary()
                .stream()
                .map(summary -> {
                    YearlyTransactionSummaryDTO impl = new YearlyTransactionSummaryDTO();
                    impl.setYear(summary.getYear());
                    impl.setAmountExpenseYear(summary.getAmountExpenseYear());
                    impl.setAmountRevenuesYear(summary.getAmountRevenuesYear());
                    impl.setAmountInvestmentsYear(summary.getAmountInvestmentsYear());
                    return impl;
                })
                .collect(Collectors.toList());
    }

    public List<ExpensesResponseDTO> findByReferenceYearAndReferenceMonth(int year, int month){

        List<Expenses> expensesList = repository.findByReferenceYearAndReferenceMonth(year, month);
        return expensesList.stream().map(mapper::toDTO).toList();
    }

    public List<ExpensesResponseDTO> findAllByYear(int year) {
        List<Expenses> expensesList = repository.findByReferenceYear(year);
        return expensesList.stream().map(mapper::toDTO).toList();
    }
}
