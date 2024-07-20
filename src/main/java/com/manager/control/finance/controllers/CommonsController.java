package com.manager.control.finance.controllers;

import com.manager.control.finance.dtos.*;
import com.manager.control.finance.services.CommonsService;
import com.manager.control.finance.services.ExpensesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commons")
public class CommonsController {

    @Autowired
    private CommonsService commonsService;

    @GetMapping("/summary-month/{year}")
    public SummaryMonthByYearDTO getAllMonthsByYear(@PathVariable int year) {
        return commonsService.getAllMonthsByYear(year);
    }

    @GetMapping("/summary-month/{year}/{month}")
    public SummaryMonthDTO getTransactionsByMonthsByYear(@PathVariable int year,@PathVariable int month) {
        return commonsService.getTransactionsByMonthsByYear(year, month);
    }

    @GetMapping("/summary-month")
    public BudgetedAccomplishedCommonDTO getTransactionsBudgetedAccomplished() {
        return commonsService.getTransactionsBudgetedAccomplished();
    }

    @GetMapping("/summary-month-year/{year}")
    public BudgetedAccomplishedCommonDTO getTransactionsBudgetedAccomplishedByYear(@PathVariable int year) {
        return commonsService.getTransactionsBudgetedAccomplishedByYear(year);
    }

}
