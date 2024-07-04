package com.manager.control.finance.dtos;

import lombok.Data;

import java.util.List;

@Data
public class SummaryMonthByYearDTO {

    private List<MonthlySumSummaryDTO> resultsMonthSummaryDTOS;
    private List<MonthlySumSummaryDTO> revenuesSummaryDTOS;
    private List<MonthlySumSummaryDTO> investmentsSummaryDTOS;
    private List<MonthlySumExpenseSummaryDTO> expenseSummaryDTOS;

}
