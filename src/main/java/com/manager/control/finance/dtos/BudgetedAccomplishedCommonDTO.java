package com.manager.control.finance.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetedAccomplishedCommonDTO {
    private List<ObjectSummary> budgeted;
    private List<ObjectSummary> expenses;
    private List<ObjectSummary> investments;
}
