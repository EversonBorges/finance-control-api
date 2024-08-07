package com.manager.control.finance.repositories;

import com.manager.control.finance.entities.Category;
import com.manager.control.finance.entities.Expenses;
import com.manager.control.finance.entities.Investments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestmentsRepository extends JpaRepository<Investments, Integer> {

    @Query("SELECT SUM(i.valueInvestments), i.referenceMonth, c.description " +
            "FROM Investments i " +
            "INNER JOIN i.category c " +
            "WHERE i.referenceYear = :year " +
            "GROUP BY i.referenceMonth, c.description " +
            "ORDER BY i.referenceMonth ASC")
    List<Object[]> sumValueInvestmentsByMonthAndCategory(int year);

    @Query("SELECT SUM(i.valueInvestments), i.referenceMonth, c.description " +
            "FROM Investments i " +
            "INNER JOIN i.category c " +
            "WHERE i.referenceYear = :year " +
            "AND i.referenceMonth = :month " +
            "GROUP BY i.referenceMonth, c.description " +
            "ORDER BY i.referenceMonth ASC")
    List<Object[]> getInvestmentsByYearAndMonthAndCategory(int year, int month);

    List<Investments> findByReferenceYearAndReferenceMonth(int year, int month);

    List<Investments> findByReferenceYear(int year);

    @Query("SELECT SUM(i.valueInvestments), i.referenceYear, c.type, c.classification " +
            "FROM Investments i " +
            "INNER JOIN i.category c " +
            "GROUP BY i.referenceYear, c.type, c.classification")
    List<Object[]> findInvestmentsSumGroupedByYearAndCategory();

    @Query("SELECT SUM(i.valueInvestments), i.referenceMonth, c.type, c.classification " +
            "FROM Investments i " +
            "INNER JOIN i.category c " +
            "WHERE i.referenceYear = :year " +
            "GROUP BY i.referenceMonth, c.type, c.classification")
    List<Object[]> findInvestmentsSumGroupedByMonthAndCategory(int year);
}