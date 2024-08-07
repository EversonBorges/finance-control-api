package com.manager.control.finance.repositories;

import com.manager.control.finance.entities.Budgeted;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BudgetedRepository extends JpaRepository<Budgeted, Integer> {

    @Query("SELECT b.budgetedAmount, b.referenceMonth, c.description, c.classification " +
            "FROM Budgeted b " +
            "INNER JOIN b.category c " +
            "WHERE b.referenceYear = :year " +
            "AND b.referenceMonth = :month " )
    List<Object[]> getBudgetedByYearAndMonthAndCategory(int year, int month);

    @Query("SELECT SUM(b.budgetedAmount), b.referenceYear, c.type, c.classification " +
            "FROM Budgeted b " +
            "INNER JOIN b.category c " +
            "GROUP BY b.referenceYear, c.type, c.classification")
    List<Object[]> findBudgetedSumGroupedByYearAndCategory();

    @Query("SELECT SUM(b.budgetedAmount), b.referenceMonth, c.type, c.classification " +
            "FROM Budgeted b " +
            "INNER JOIN b.category c " +
            "WHERE b.referenceYear = :year " +
            "GROUP BY b.referenceMonth, c.type, c.classification")
    List<Object[]> findBudgetedSumGroupedByMonthAndCategory(int year);

}