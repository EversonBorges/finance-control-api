package com.manager.control.finance.repositories;

import com.manager.control.finance.entities.Expenses;
import com.manager.control.finance.entities.Revenues;
import com.manager.control.finance.enums.PaymentMethodsEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.manager.control.finance.interfaces.YearlyTransactionSummary;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ExpensesRepository extends JpaRepository<Expenses, Integer> {
    List<Expenses> findByTransactionDateGreaterThanEqualAndPaymentMethodsIn(LocalDate startDate,
                                                                            List<PaymentMethodsEnum> paymentMethods);
    @Query(value = """
        SELECT
          COALESCE(expenses.reference_year, revenues.receiving_year, investments.investments_year) AS year,  
          COALESCE(expenses.total_expense, 0) AS amountExpenseYear,  
          COALESCE(revenues.total_revenue, 0) AS amountRevenuesYear,
          COALESCE(investments.total_investments, 0) AS amountInvestmentsYear
        FROM 
          (SELECT e.reference_year, SUM(e.values_installment) AS total_expense  
           FROM expenses e GROUP BY e.reference_year) expenses
        FULL OUTER JOIN  
          (SELECT r.reference_year AS receiving_year, SUM(r.amount) AS total_revenue  
           FROM revenues r GROUP BY r.reference_year) revenues
        ON expenses.reference_year = revenues.receiving_year
        FULL OUTER JOIN
          (SELECT i.reference_year AS investments_year, SUM(i.value_investments) AS total_investments  
           FROM investments i GROUP BY i.reference_year) investments
        ON COALESCE(expenses.reference_year, revenues.receiving_year) = investments.investments_year
        ORDER BY year;
        """, nativeQuery = true)
    List<YearlyTransactionSummary> findYearlyTransactionSummary();

    List<Expenses> findByTransactionControl(UUID transactionControl);

    @Query("SELECT SUM(e.valuesInstallment), e.referenceMonth, c.description, c.classification " +
            "FROM Expenses e " +
            "INNER JOIN e.category c " +
            "WHERE e.referenceYear = :year " +
            "GROUP BY e.referenceMonth, c.description, c.classification " +
            "ORDER BY e.referenceMonth ASC, c.classification ASC")
    List<Object[]> sumValuesByMonthAndCategory(int year);

    @Query("SELECT SUM(e.valuesInstallment), e.referenceMonth, c.description, c.classification " +
            "FROM Expenses e " +
            "INNER JOIN e.category c " +
            "WHERE e.referenceYear = :year " +
            "AND e.referenceMonth = :month " +
            "GROUP BY e.referenceMonth, c.description, c.classification " +
            "ORDER BY e.referenceMonth ASC, c.classification ASC")
    List<Object[]> getExpensesByYearAndMonthAndCategory(int year, int month);

    @Query("SELECT SUM(e.valuesInstallment), e.referenceMonth, cc.name AS description " +
            "FROM Expenses e " +
            "JOIN e.creditCard cc " +
            "WHERE e.creditCard.id IS NOT NULL " +
            "AND e.referenceYear = :year " +
            "AND e.referenceMonth = :month " +
            "GROUP BY e.referenceMonth, cc.id")
    List<Object[]> sumValuesByCreditCardAndMonth( int  year, int month);

    List<Expenses> findByReferenceYearAndReferenceMonth(int year, int month);

    List<Expenses> findByReferenceYear(int year);

    @Query("SELECT SUM(e.valuesInstallment), e.referenceYear, c.type, c.classification " +
            "FROM Expenses e " +
            "INNER JOIN e.category c " +
            "GROUP BY e.referenceYear, c.type, c.classification")
    List<Object[]> findExpensesSumGroupedByYearAndCategory();

    @Query("SELECT SUM(e.valuesInstallment), e.referenceMonth, c.type, c.classification " +
            "FROM Expenses e " +
            "INNER JOIN e.category c " +
            "WHERE e.referenceYear = :year " +
            "GROUP BY e.referenceMonth, c.type, c.classification")
    List<Object[]> findExpensesSumGroupedByMonthAndCategory(int year);

}
