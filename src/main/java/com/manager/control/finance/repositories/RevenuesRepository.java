package com.manager.control.finance.repositories;

import com.manager.control.finance.entities.Revenues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RevenuesRepository extends JpaRepository<Revenues, Integer> {

    @Query("SELECT rv FROM Revenues rv JOIN rv.category ca " +
            "WHERE ca.description = 'Adiantamento' " +
            "AND FUNCTION('YEAR', rv.receivingDate) = :year " +
            "AND FUNCTION('MONTH', rv.receivingDate) = :month")
    Optional<Revenues> findByCategoryDescriptionAndYearAndMonth(@Param("year") int year,
                                                               @Param("month") int month);

}
