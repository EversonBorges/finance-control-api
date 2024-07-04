package com.manager.control.finance.dtos;

import lombok.Data;

import java.util.List;

@Data
public class SummaryMonthDTO {


    private List<MonthlySumSummaryDTO> resultsMonthSummaryDTOS;
    private List<MonthlySumSummaryDTO> revenuesSummaryDTOS;
    private List<MonthlySumSummaryDTO> investmentsSummaryDTOS;
    private List<MonthlySummaryCommonDTO> expenseSummaryDTOS;
    private List<MonthlySummaryCommonDTO> budgetedSummaryDTOS;
    private List<MonthlySumSummaryDTO> creditCardSummaryDTOS;

}
