package com.manager.control.finance.repositories;

import com.manager.control.finance.entities.Revenues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RevenuesRepository extends JpaRepository<Revenues, Integer> {

    @Query("SELECT rv FROM Revenues rv JOIN rv.category ca " +
            "WHERE ca.description = 'Adiantamento' " +
            "AND EXTRACT(YEAR FROM rv.receivingDate) = :year " +
            "AND EXTRACT(MONTH FROM rv.receivingDate) = :month "+
            "AND rv.updateJob = false")
    Optional<Revenues> findByCategoryDescriptionAndYearAndMonth(@Param("year") int year,
                                                               @Param("month") int month);

    @Query("SELECT SUM(r.amount), r.referenceMonth, c.description " +
            "FROM Revenues r " +
            "INNER JOIN r.category c " +
            "WHERE r.referenceYear = :year " +
            "GROUP BY r.referenceMonth, c.description " +
            "ORDER BY r.referenceMonth ASC")
    List<Object[]> sumAmountByMonthAndCategory(int year);

    @Query("SELECT SUM(r.amount), r.referenceMonth, c.description " +
            "FROM Revenues r " +
            "INNER JOIN r.category c " +
            "WHERE r.referenceYear = :year " +
            "AND r.referenceMonth = :month " +
            "GROUP BY r.referenceMonth, c.description " +
            "ORDER BY r.referenceMonth ASC")
    List<Object[]> getRevenuesByYearAndMonthAndCategory(int year, int month);

    List<Revenues> findByReferenceYearAndReferenceMonth(int year, int month);

    List<Revenues> findByReferenceYear(int year);
}
