package com.manager.control.finance.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlySummaryCommonDTO {
    private int referenceMonth;
    private List<ObjectSummary> essentialSum;
    private List<ObjectSummary> nonEssentialSum;
    private List<ObjectSummary> withoutClassification;
}
