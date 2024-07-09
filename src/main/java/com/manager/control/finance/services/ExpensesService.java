package com.manager.control.finance.services;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.manager.control.finance.Mappers.ExpensesMapper;
import com.manager.control.finance.dtos.*;
import com.manager.control.finance.entities.CreditCard;
import com.manager.control.finance.entities.Expenses;
import com.manager.control.finance.entities.Revenues;
import com.manager.control.finance.enums.PaymentMethodsEnum;
import com.manager.control.finance.exceptions.DataNotFoundException;
import com.manager.control.finance.repositories.BudgetedRepository;
import com.manager.control.finance.repositories.ExpensesRepository;
import com.manager.control.finance.repositories.InvestmentsRepository;
import com.manager.control.finance.repositories.RevenuesRepository;
import com.manager.control.finance.utils.GlobalMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExpensesService {

    @Autowired
    private ExpensesRepository repository;

    @Autowired
    private InvestmentsRepository investmentsRepository;

    @Autowired
    private RevenuesRepository revenuesRepository;

    @Autowired
    private BudgetedRepository budgetedRepository;

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
        var dueDate = LocalDate.of(year, dto.transactionDate().getMonth().plus(GlobalMessages.ONE).getValue(), dueDay);
        LocalDate bestOfDayPurchase = dueDate.minusDays(referenceDayPurchase);
        long diffDays = ChronoUnit.DAYS.between(dto.transactionDate(), bestOfDayPurchase);

        if(diffDays > GlobalMessages.MAX_DAY){
            dueDate = LocalDate.of(year, dto.transactionDate().getMonth().getValue(), dueDay);
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
        List<PaymentMethodsEnum> paymentMethods = Arrays.asList(
                PaymentMethodsEnum.DEBIT_CARD,
                PaymentMethodsEnum.PIX,
                PaymentMethodsEnum.ACCOUNT_DISCOUNT);

        return repository.findByTransactionDateGreaterThanEqualAndPaymentMethodsIn(date,paymentMethods);
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

    public SummaryMonthByYearDTO getAllMonthsByYear(int year){
        SummaryMonthByYearDTO summaryMonthByYearDTO = new SummaryMonthByYearDTO();

        List<Object[]> expenseObjects = repository.sumValuesByMonthAndCategory(year);
        List<MonthlySumExpenseSummaryDTO>  expenseSummary = buildSumExpensesSummary(expenseObjects);
        summaryMonthByYearDTO.setExpenseSummaryDTOS(expenseSummary);

        List<Object[]> investmentsObjects  = investmentsRepository.sumValueInvestmentsByMonthAndCategory(year);
        List<MonthlySumSummaryDTO> investmentsSummarylist = buildSumSummary(investmentsObjects);
        summaryMonthByYearDTO.setInvestmentsSummaryDTOS(investmentsSummarylist);

        List<Object[]> revenuesObjects  = revenuesRepository.sumAmountByMonthAndCategory(year);
        List<MonthlySumSummaryDTO> revenuesSummarylist = buildSumSummary(revenuesObjects);
        summaryMonthByYearDTO.setRevenuesSummaryDTOS(revenuesSummarylist);

        List<MonthlySumSummaryDTO> resultsMonth = buildResultsMonth(expenseSummary, revenuesSummarylist);
        summaryMonthByYearDTO.setResultsMonthSummaryDTOS(resultsMonth);

        return summaryMonthByYearDTO;
    }

    public SummaryMonthDTO getTransactionsByMonthsByYear(int year, int month){
        SummaryMonthDTO summaryMonthDTO = new SummaryMonthDTO();

        List<Object[]> expenseObjects = repository.getExpensesByYearAndMonthAndCategory(year, month);
        List<MonthlySummaryCommonDTO>  expenseSummary = buildExpensesSummary(expenseObjects);
        summaryMonthDTO.setExpenseSummaryDTOS(expenseSummary);

        List<Object[]> budgetedObjects = budgetedRepository.getBudgetedByYearAndMonthAndCategory(year, month);
        List<MonthlySummaryCommonDTO>  budgetedSummary = buildExpensesSummary(budgetedObjects);
        summaryMonthDTO.setBudgetedSummaryDTOS(budgetedSummary);

        List<Object[]> investmentsObjects  = investmentsRepository.getInvestmentsByYearAndMonthAndCategory(year, month);
        List<MonthlySumSummaryDTO> investmentsSummarylist = buildSummary(investmentsObjects);
        summaryMonthDTO.setInvestmentsSummaryDTOS(investmentsSummarylist);

        List<Object[]> revenuesObjects  = revenuesRepository.getRevenuesByYearAndMonthAndCategory(year,month);
        List<MonthlySumSummaryDTO> revenuesSummarylist = buildSummary(revenuesObjects);
        summaryMonthDTO.setRevenuesSummaryDTOS(revenuesSummarylist);

        List<Object[]> creditCardObjects  = repository.sumValuesByCreditCardAndMonth(year,month);
        List<MonthlySumSummaryDTO> creditCardSummarylist = buildSummary(creditCardObjects);
        summaryMonthDTO.setCreditCardSummaryDTOS(creditCardSummarylist);

        return summaryMonthDTO;
    }

    private List<MonthlySumSummaryDTO> buildResultsMonth(List<MonthlySumExpenseSummaryDTO> expenseSummary, List<MonthlySumSummaryDTO> revenuesSummary) {
        Map<Integer, BigDecimal> expensesByMonth = expenseSummary.stream()
                .collect(Collectors.toMap(
                        MonthlySumExpenseSummaryDTO::getReferenceMonth,
                        e -> e.getEssentialSum().add(e.getNonEssentialSum()),
                        BigDecimal::add
                ));

        Map<Integer, BigDecimal> differencesByMonth = revenuesSummary.stream()
                .collect(Collectors.toMap(
                        MonthlySumSummaryDTO::getReferenceMonth,
                        r -> r.getSum().subtract(expensesByMonth.getOrDefault(r.getReferenceMonth(), BigDecimal.ZERO))
                ));

        List<MonthlySumSummaryDTO> summaryDTOS =  new ArrayList<>();
        differencesByMonth.forEach((month, difference) -> {
            MonthlySumSummaryDTO dto = new MonthlySumSummaryDTO(difference, month);
            summaryDTOS.add(dto);
        });

        return summaryDTOS;
    }

    private List<MonthlySumSummaryDTO> buildSummary(List<Object[]> list){
        List<ObjectSummary> summaryList = buildSummaryList(list);

        List<MonthlySumSummaryDTO> summaryDTOS = new ArrayList<>();

        summaryList.forEach(item ->{
            summaryDTOS.add(new MonthlySumSummaryDTO(
                    item.getSum(),
                    item.getReferenceMonth(),
                    item.getDescription()
            ));
        });

        return summaryDTOS;
    }

    private List<MonthlySumSummaryDTO> buildSumSummary(List<Object[]> list){
        List<ObjectSummary> summaryList = buildSummaryList(list);
        Map<Integer, BigDecimal> summedByMonth = summaryList.stream()
                .collect(Collectors.groupingBy(
                        ObjectSummary::getReferenceMonth,
                        Collectors.mapping(
                                ObjectSummary::getSum,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));

        return summedByMonth.entrySet().stream()
                .map(entry -> new MonthlySumSummaryDTO(entry.getValue(), entry.getKey()))
                .toList();
    }

    private List<MonthlySummaryCommonDTO> buildExpensesSummary(List<Object[]> objects) {
        List<ObjectSummary> summaryList = buildSummaryList(objects);

        Map<Integer, MonthlySummaryCommonDTO> monthlySummaries = summaryList.stream()
                .collect(Collectors.groupingBy(
                        ObjectSummary::getReferenceMonth,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                expenses -> {
                                    List<ObjectSummary> essentialExpenses = expenses.stream()
                                            .filter(expense -> GlobalMessages.ESSENTIALS.equals(expense.getClassification()))
                                            .collect(Collectors.toList());

                                    List<ObjectSummary> nonEssentialExpenses = expenses.stream()
                                            .filter(expense -> GlobalMessages.NO_ESSENTIALS.equals(expense.getClassification()))
                                            .collect(Collectors.toList());

                                    List<ObjectSummary> withoutClassification = expenses.stream()
                                            .filter(expense -> expense.getClassification() == null)
                                            .toList();

                                    return new MonthlySummaryCommonDTO(
                                            expenses.get(0).getReferenceMonth(),
                                            essentialExpenses,
                                            nonEssentialExpenses,
                                            withoutClassification
                                    );
                                }
                        )
                ));

        List<MonthlySummaryCommonDTO> expenseSummary = new ArrayList<>();
        monthlySummaries.forEach((month, summary) -> {
            MonthlySummaryCommonDTO dto =
                    new MonthlySummaryCommonDTO(month,
                            summary.getEssentialSum(),
                            summary.getNonEssentialSum(),
                            summary.getWithoutClassification()
                    );
            expenseSummary.add(dto);
        });

        return expenseSummary;
    }

    private List<MonthlySumExpenseSummaryDTO> buildSumExpensesSummary(List<Object[]> objects) {
        List<ObjectSummary> summaryList = buildSummaryList(objects);

        Map<Integer, MonthlySumExpenseSummaryDTO> monthlySummaries = summaryList.stream()
                .collect(Collectors.groupingBy(
                        ObjectSummary::getReferenceMonth,
                        Collectors.reducing(new MonthlySumExpenseSummaryDTO(),
                                expense -> new MonthlySumExpenseSummaryDTO(
                                        expense.getReferenceMonth(),
                                        expense.getSum().multiply(GlobalMessages.ESSENTIALS.equals(expense.getClassification())
                                                ? BigDecimal.ONE : BigDecimal.ZERO),
                                        expense.getSum().multiply(GlobalMessages.NO_ESSENTIALS.equals(expense.getClassification())
                                                ? BigDecimal.ONE : BigDecimal.ZERO)),
                                (s1, s2) -> new MonthlySumExpenseSummaryDTO(
                                        s1.getReferenceMonth(),
                                        s1.getEssentialSum().add(s2.getEssentialSum()),
                                        s1.getNonEssentialSum().add(s2.getNonEssentialSum()))
                        )
                ));

        List<MonthlySumExpenseSummaryDTO> expenseSummary = new ArrayList<>();
        monthlySummaries.forEach((month, summary) -> {
            MonthlySumExpenseSummaryDTO dto =
                    new MonthlySumExpenseSummaryDTO(month, summary.getEssentialSum(),summary.getNonEssentialSum());
            expenseSummary.add(dto);
        });

        return expenseSummary;
    }

    private List<ObjectSummary> buildSummaryList(List<Object[]> resultList) {
        List<ObjectSummary> sumValuesList = new ArrayList<>();

        resultList.forEach(result -> {
            ObjectSummary summary = new ObjectSummary();

            summary.setSum(result[0] != null ? (BigDecimal) result[0]: null); // Assume que o primeiro campo é BigDecimal
            summary.setReferenceMonth(result[1] != null ? (Integer) result[1]: 0); // Assume que o segundo campo é Integer
            summary.setDescription(result[2] != null ? (String) result[2]: null); // Assume que o terceiro campo é String

            if(result.length == 4){
                summary.setClassification(result[3] != null ?(String) result[3]: null); // Assume que o quarto campo é String
            }
            sumValuesList.add(summary);
        });

        return sumValuesList;
    }

    public List<ExpensesResponseDTO> findByReferenceYearAndReferenceMonth(int year, int month){

        List<Expenses> expensesList = repository.findByReferenceYearAndReferenceMonth(year, month);
        return expensesList.stream().map(mapper::toDTO).toList();
    }
}
