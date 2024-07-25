package com.manager.control.finance.repositories;

import com.manager.control.finance.entities.Category;
import com.manager.control.finance.entities.JobManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobManagerRepository extends JpaRepository<JobManager, Integer> {

    List<Optional<JobManager>> findByName(String name);

    @Query(value = """
            SELECT
                (SELECT sum(r.amount)
                 FROM revenues r
                 WHERE reference_month = :month AND reference_year = :year) -
                (SELECT sum(e.values_installment)
                 FROM expenses e
                 WHERE reference_month = :month AND reference_year = :year) AS saldo;
            """, nativeQuery = true)
    BigDecimal getMonthlyBalance(int year, int month);
}
