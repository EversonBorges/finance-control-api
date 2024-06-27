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
            "AND EXTRACT(YEAR FROM rv.receivingDate) = :year " +
            "AND EXTRACT(MONTH FROM rv.receivingDate) = :month "+
            "AND rv.updateJob = false")
    Optional<Revenues> findByCategoryDescriptionAndYearAndMonth(@Param("year") int year,
                                                               @Param("month") int month);

}
