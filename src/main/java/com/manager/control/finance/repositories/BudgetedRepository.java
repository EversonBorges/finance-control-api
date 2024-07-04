package com.manager.control.finance.repositories;

import com.manager.control.finance.entities.Budgeted;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BudgetedRepository extends JpaRepository<Budgeted, Integer> {

    @Query("SELECT SUM(b.budgetedAmount), b.referenceMonth, c.description, c.classification " +
            "FROM Budgeted b " +
            "INNER JOIN b.category c " +
            "WHERE b.referenceYear = :year " +
            "AND b.referenceMonth = :month " +
            "GROUP BY b.referenceMonth, c.description, c.classification " +
            "ORDER BY b.referenceMonth ASC, c.classification ASC")
    List<Object[]> getBudgetedByYearAndMonthAndCategory(int year, int month);
}