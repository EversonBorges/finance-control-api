package com.manager.control.finance.services;

import com.manager.control.finance.dtos.*;
import com.manager.control.finance.repositories.BudgetedRepository;
import com.manager.control.finance.repositories.ExpensesRepository;
import com.manager.control.finance.repositories.InvestmentsRepository;
import com.manager.control.finance.repositories.RevenuesRepository;
import com.manager.control.finance.utils.GlobalMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommonsService {

    @Autowired
    private ExpensesRepository expensesRepository;

    @Autowired
    private InvestmentsRepository investmentsRepository;

    @Autowired
    private RevenuesRepository revenuesRepository;

    @Autowired
    private BudgetedRepository budgetedRepository;

    public SummaryMonthByYearDTO getAllMonthsByYear(int year){
        SummaryMonthByYearDTO summaryMonthByYearDTO = new SummaryMonthByYearDTO();

        List<Object[]> expenseObjects = expensesRepository.sumValuesByMonthAndCategory(year);
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

        List<Object[]> expenseObjects = expensesRepository.getExpensesByYearAndMonthAndCategory(year, month);
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

        List<Object[]> creditCardObjects  = expensesRepository.sumValuesByCreditCardAndMonth(year,month);
        List<MonthlySumSummaryDTO> creditCardSummarylist = buildSummary(creditCardObjects);
        summaryMonthDTO.setCreditCardSummaryDTOS(creditCardSummarylist);

        return summaryMonthDTO;
    }

    public BudgetedAccomplishedCommonDTO getTransactionsBudgetedAccomplished(){
        List<Object[]> budgetedObjects = budgetedRepository.findBudgetedSumGroupedByYearAndCategory();
        List<ObjectSummary> budgetedObjectsList = buildSummaryList(budgetedObjects);
        List<Object[]> expensesObjects = expensesRepository.findExpensesSumGroupedByYearAndCategory();
        List<ObjectSummary> expensesObjectsList = buildSummaryList(expensesObjects);
        List<Object[]> investmentsObjects = investmentsRepository.findInvestmentsSumGroupedByYearAndCategory();
        List<ObjectSummary> investmentsObjectsList = buildSummaryList(investmentsObjects);

        return new BudgetedAccomplishedCommonDTO(
                budgetedObjectsList,
                expensesObjectsList,
                investmentsObjectsList
        );
    }

    public BudgetedAccomplishedCommonDTO getTransactionsBudgetedAccomplishedByYear(int year){
        List<Object[]> budgetedObjects = budgetedRepository.findBudgetedSumGroupedByMonthAndCategory(year);
        List<ObjectSummary> budgetedObjectsList = buildSummaryList(budgetedObjects);
        List<Object[]> expensesObjects = expensesRepository.findExpensesSumGroupedByMonthAndCategory(year);
        List<ObjectSummary> expensesObjectsList = buildSummaryList(expensesObjects);
        List<Object[]> investmentsObjects = investmentsRepository.findInvestmentsSumGroupedByMonthAndCategory(year);
        List<ObjectSummary> investmentsObjectsList = buildSummaryList(investmentsObjects);

        return new BudgetedAccomplishedCommonDTO(
                budgetedObjectsList,
                expensesObjectsList,
                investmentsObjectsList
        );
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

    private List<ObjectSummary> buildSummaryList(List<Object[]> resultList) {
        List<ObjectSummary> sumValuesList = new ArrayList<>();

        resultList.forEach(result -> {
            ObjectSummary summary = new ObjectSummary();

            summary.setSum(result[0] != null ? (BigDecimal) result[0]: null); // Assume que o primeiro campo é BigDecimal
            summary.setReferenceMonth(result[1] != null ? (Integer) result[1]: 0); // Assume que o segundo campo é Integer
            if(result[2].getClass().getName().equals("java.lang.Character")){
                summary.setDescription(result[2].toString()); // Assume que o terceiro campo é String
            }else{
                summary.setDescription((String) result[2]); // Assume que o terceiro campo é String
            }

            if(result.length == 4){
                summary.setClassification(result[3] != null ?(String) result[3]: null); // Assume que o quarto campo é String
            }
            sumValuesList.add(summary);
        });

        return sumValuesList;
    }
}
