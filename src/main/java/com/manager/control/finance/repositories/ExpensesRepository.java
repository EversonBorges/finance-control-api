package com.manager.control.finance.repositories;

import com.manager.control.finance.entities.Expenses;
import com.manager.control.finance.enums.PaymentMethodsEnum;
import com.manager.control.finance.interfaces.YearlyTransactionSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpensesRepository extends JpaRepository<Expenses, Integer> {
    List<Expenses> findByTransactionDateGreaterThanEqualAndPaymentMethodsIn(LocalDate startDate,
                                                                            List<PaymentMethodsEnum> paymentMethods);
    @Query(value =
            "SELECT " +
                    "    COALESCE(expenses.reference_year, revenues.receiving_year) AS year, " +
                    "    COALESCE(expenses.total_expense, 0) AS amountExpenseYear, " +
                    "    COALESCE(revenues.total_revenue, 0) AS amountRevenuesYear " +
                    "FROM " +
                    "    (SELECT " +
                    "         e.reference_year, " +
                    "         SUM(e.values_installment) AS total_expense " +
                    "     FROM " +
                    "         expenses e " +
                    "     GROUP BY " +
                    "         e.reference_year) expenses " +
                    "FULL OUTER JOIN " +
                    "    (SELECT " +
                    "         EXTRACT(YEAR FROM r.receiving_date) AS receiving_year, " +
                    "         SUM(r.amount) AS total_revenue " +
                    "     FROM " +
                    "         revenues r " +
                    "     GROUP BY " +
                    "         EXTRACT(YEAR FROM r.receiving_date)) revenues " +
                    "ON " +
                    "    expenses.reference_year = revenues.receiving_year " +
                    "ORDER BY " +
                    "    year",
            nativeQuery = true)
    List<YearlyTransactionSummary> findYearlyTransactionSummary();
}
